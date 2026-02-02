package com.xinyihl.whimcraft.common.items.cell.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageEssentiaCell;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;
import com.xinyihl.whimcraft.common.storage.InfinityStorageWorldData;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

import java.math.BigInteger;
import java.util.UUID;

public class InfinityStorageEssentiaCellInventory implements IMEInventoryHandler<IAEEssentiaStack> {
    private final UUID uuid;
    private final ItemStack container;
    private final ISaveProvider saveProvider;

    public InfinityStorageEssentiaCellInventory(ItemStack container, ISaveProvider saveProvider) {
        this.uuid = InfinityStorageEssentiaCell.getOrCreateUuid(container);
        this.container = container;
        this.saveProvider = saveProvider;
    }

    private InfinityStorageWorldData getWorldData() {
        return InfinityStorageWorldData.get(DimensionManager.getWorld(0));
    }

    @Override
    public IAEEssentiaStack injectItems(IAEEssentiaStack input, Actionable actionable, IActionSource src) {
        if (input == null || input.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Object2LongMap<IAEStack> storage = worldData.getStorage(uuid);
        IAEStack key = input.copy();
        key.setStackSize(1);
        boolean existingKey = storage.containsKey(key);
        if (actionable == Actionable.MODULATE) {
            if (!existingKey) {
                storage.put(key, input.getStackSize());
                updateStats();
            } else {
                storage.compute(key, (k, currentCount) -> currentCount + input.getStackSize());
            }
            worldData.markDirty();
        }
        return null;
    }

    @Override
    public IAEEssentiaStack extractItems(IAEEssentiaStack request, Actionable actionable, IActionSource src) {
        if (request == null || request.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Object2LongMap<IAEStack> storage = worldData.getStorage(uuid);
        IAEStack key = request.copy();
        key.setStackSize(1);
        boolean existingKey = storage.containsKey(key);
        if (!existingKey) {
            return null;
        }
        long currentCount = storage.getLong(key);
        long extractAmount = Math.min(currentCount, request.getStackSize());
        if (extractAmount <= 0) {
            return null;
        }
        if (actionable == Actionable.MODULATE) {
            long remaining = currentCount - extractAmount;
            if (remaining <= 0) {
                storage.remove(key);
                updateStats();
            } else {
                storage.put(key, remaining);
            }
            worldData.markDirty();
        }
        IAEEssentiaStack result = (IAEEssentiaStack) key.copy();
        result.setStackSize(extractAmount);
        return result;
    }

    @Override
    public IItemList<IAEEssentiaStack> getAvailableItems(IItemList<IAEEssentiaStack> out) {
        Object2LongMap<IAEStack> storage = getWorldData().getStorage(uuid);
        for (Object2LongMap.Entry<IAEStack> entry : storage.object2LongEntrySet()) {
            IAEStack stack = entry.getKey();
            if (stack instanceof IAEEssentiaStack) {
                IAEEssentiaStack ess = ((IAEEssentiaStack) stack).copy();
                ess.setStackSize(entry.getLongValue());
                out.add(ess);
            }
        }
        return out;
    }

    @Override
    public IStorageChannel<IAEEssentiaStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    private void updateStats() {
        Object2LongMap<IAEStack> storage = getWorldData().getStorage(uuid);
        int totalTypes = 0;
        BigInteger totalBytes = BigInteger.ZERO;
        for (Object2LongMap.Entry<IAEStack> entry : storage.object2LongEntrySet()) {
            totalTypes++;
            totalBytes = totalBytes.add(BigInteger.valueOf(entry.getLongValue()));
        }
        NBTTagCompound tag = container.getTagCompound();
        if (tag != null) {
            tag.setInteger(InfinityStorageCellBase.NBT_TYPES, totalTypes);
            tag.setString(InfinityStorageCellBase.NBT_BYTES, totalBytes.toString());
        }
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEEssentiaStack iaeEssentiaStack) {
        return false;
    }

    @Override
    public boolean canAccept(IAEEssentiaStack iaeEssentiaStack) {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true;
    }
}
