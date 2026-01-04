package com.xinyihl.whimcraft.common.tile.base;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.redis.ItemStackSerde;
import com.xinyihl.whimcraft.common.redis.RedisIoClient;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        tickCounter++;
        if (tickCounter % Configurations.REDIS_IO_CONFIG.tick != 0) {
            return;
        }
        if (!RedisIoClient.isEnabled()) {
            return;
        }
        try {
            doRedisSync();
        } catch (Exception ignored) {
            // 不让 Redis 异常导致崩服
        }
    }

    public abstract String getRedisKeyType();

    protected abstract void doRedisSync() throws Exception;

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
            if ("input".equals(getRedisKeyType())) {
                return (T) insertOnly;
            }
            if ("output".equals(getRedisKeyType())) {
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

    protected static void pushStacksToRedisQueue(Jedis jedis, String key, ItemStackHandler inv) throws Exception {
        int maxEntries = Math.max(0, Configurations.REDIS_IO_CONFIG.maxEntries);
        long len = jedis.llen(key);
        for (int i = 0; i < inv.getSlots(); i++) {
            if (maxEntries > 0 && len >= maxEntries) {
                break;
            }
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            String payload = ItemStackSerde.toBase64(stack);
            jedis.rpush(key, payload);
            inv.setStackInSlot(i, ItemStack.EMPTY);
            len++;
        }
    }

    protected static void popStacksFromRedisQueue(Jedis jedis, String key, ItemStackHandler inv) throws Exception {
        while (true) {
            String payload = jedis.lpop(key);
            if (payload == null) {
                return;
            }
            ItemStack stack = ItemStackSerde.fromBase64(payload);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack remainder = insertPreferStacking(inv, stack);
            if (!remainder.isEmpty()) {
                jedis.lpush(key, ItemStackSerde.toBase64(remainder));
                return;
            }
        }
    }

    private static ItemStack insertPreferStacking(ItemStackHandler inv, ItemStack stack) {
        ItemStack remaining = stack;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack existing = inv.getStackInSlot(i);
            if (existing.isEmpty()) {
                continue;
            }
            if (!canStacksMerge(existing, remaining)) {
                continue;
            }
            remaining = inv.insertItem(i, remaining, false);
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                continue;
            }
            remaining = inv.insertItem(i, remaining, false);
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return remaining;
    }

    private static boolean canStacksMerge(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        if (!ItemStack.areItemsEqual(a, b)) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(a, b);
    }
}
