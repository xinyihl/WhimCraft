package com.xinyihl.whimcraft.common.tile.base;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.redis.RedisClient;
import com.xinyihl.whimcraft.common.utils.ItemStackSerde;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class TileRedisInterfaceBase extends TileEntity implements ITickable {

    public static final int ROWS = 5;
    public static final int COLUMNS = 9;
    public static final int SIZE = ROWS * COLUMNS;

    protected final ItemStackHandler inventory = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private final IItemHandler insertOnly = new InsertOnlyHandler(inventory);
    private final IItemHandler extractOnly = new ExtractOnlyHandler(inventory);

    protected UUID uuid;
    private int tickCounter;

    protected void pushStacksToRedisQueue(Jedis jedis, String key) throws Exception {
        int maxEntries = Math.max(0, Configurations.REDIS_IO_CONFIG.maxEntries);
        long len = jedis.llen(key);
        Pipeline p = jedis.pipelined();
        List<Integer> toClear = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (maxEntries > 0 && len >= maxEntries) {
                break;
            }
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            String payload = ItemStackSerde.toBase64(stack);
            p.rpush(key, payload);
            toClear.add(i);
            len++;
        }
        p.sync();
        for (int idx : toClear) {
            inventory.setStackInSlot(idx, ItemStack.EMPTY);
        }
    }

    protected void popStacksFromRedisQueue(Jedis jedis, String key) throws Exception {
        while (true) {
            String payload = jedis.lpop(key);
            if (payload == null) {
                return;
            }
            ItemStack stack = ItemStackSerde.fromBase64(payload);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack remainder = insertPreferStacking(stack);
            if (!remainder.isEmpty()) {
                jedis.lpush(key, ItemStackSerde.toBase64(remainder));
                return;
            }
        }
    }

    private ItemStack insertPreferStacking(ItemStack stack) {
        ItemStack remaining = stack;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack existing = inventory.getStackInSlot(i);
            if (existing.isEmpty()) {
                continue;
            }
            if (!canStacksMerge(existing, remaining)) {
                continue;
            }
            remaining = inventory.insertItem(i, remaining, false);
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                continue;
            }
            remaining = inventory.insertItem(i, remaining, false);
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return remaining;
    }

    private boolean canStacksMerge(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        if (!ItemStack.areItemsEqual(a, b)) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(a, b);
    }

    public UUID ensureUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
            markDirty();
        }
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        markDirty();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    protected String redisKey() {
        String prefix = Configurations.REDIS_IO_CONFIG.keyPrefix;
        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = "whimcraft:io";
        }
        return prefix + ":" + uuid.toString();
    }

    @Override
    public void update() {
        if (world == null || world.isRemote) {
            return;
        }
        if (!RedisClient.isOnline) {
            return;
        }
        tickCounter++;
        if (tickCounter % Configurations.REDIS_IO_CONFIG.tick != 0) {
            return;
        }
        try {
            doSync();
        } catch (Exception ignored) {
            // 不让 Redis 异常导致崩服
        }
    }

    public abstract String getType();

    protected abstract void doSync() throws Exception;

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasUniqueId("redis_uuid")) {
            this.uuid = compound.getUniqueId("redis_uuid");
        }
        if (compound.hasKey("inv")) {
            inventory.deserializeNBT(compound.getCompoundTag("inv"));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (uuid != null) {
            compound.setUniqueId("redis_uuid", uuid);
        }
        compound.setTag("inv", inventory.serializeNBT());
        return compound;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if ("input".equals(getType())) {
                return (T) insertOnly;
            }
            if ("output".equals(getType())) {
                return (T) extractOnly;
            }
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    protected static final class InsertOnlyHandler implements IItemHandler {
        private final IItemHandler delegate;

        private InsertOnlyHandler(IItemHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getSlots() {
            return delegate.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return delegate.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return delegate.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return delegate.getSlotLimit(slot);
        }
    }

    protected static final class ExtractOnlyHandler implements IItemHandler {
        private final IItemHandler delegate;

        private ExtractOnlyHandler(IItemHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getSlots() {
            return delegate.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return delegate.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return delegate.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return delegate.getSlotLimit(slot);
        }
    }
}
