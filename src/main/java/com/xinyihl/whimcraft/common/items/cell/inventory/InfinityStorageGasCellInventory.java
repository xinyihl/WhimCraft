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
import com.mekeng.github.common.me.data.IAEGasStack;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageGasCell;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;
import com.xinyihl.whimcraft.common.storage.InfinityStorageWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class InfinityStorageGasCellInventory implements IMEInventoryHandler<IAEGasStack> {
    private final UUID uuid;
    private final ItemStack container;
    private ISaveProvider saveProvider;

    public InfinityStorageGasCellInventory(ItemStack container, ISaveProvider saveProvider) {
        this.uuid = InfinityStorageGasCell.getOrCreateUuid(container);
        this.container = container;
        this.saveProvider = saveProvider;
    }

    private InfinityStorageWorldData getWorldData() {
        return InfinityStorageWorldData.get(DimensionManager.getWorld(0));
    }

    @Override
    public IAEGasStack injectItems(IAEGasStack input, Actionable actionable, IActionSource src) {
        if (input == null || input.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Map<IAEStack, Long> storage = worldData.getStorage(uuid);
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
    public IAEGasStack extractItems(IAEGasStack request, Actionable actionable, IActionSource src) {
        if (request == null || request.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Map<IAEStack, Long> storage = worldData.getStorage(uuid);
        IAEStack key = request.copy();
        key.setStackSize(1);
        boolean existingKey = storage.containsKey(request);
        if (!existingKey) {
            return null;
        }
        long currentCount = storage.get(key);
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
        IAEGasStack result = (IAEGasStack) key.copy();
        result.setStackSize(extractAmount);
        return result;
    }

    @Override
    public IItemList<IAEGasStack> getAvailableItems(IItemList<IAEGasStack> out) {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            IAEStack stack = entry.getKey();
            if (stack instanceof IAEGasStack) {
                IAEGasStack gasStack = (IAEGasStack) stack.copy();
                gasStack.setStackSize(entry.getValue());
                out.add(gasStack);
            }
        }
        return out;
    }

    @Override
    public IStorageChannel<IAEGasStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class);
    }

    private void updateStats() {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        int gasTypes = 0;
        BigInteger totalBytes = BigInteger.ZERO;
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            if (entry.getKey() instanceof IAEGasStack) {
                gasTypes++;
                totalBytes = totalBytes.add(BigInteger.valueOf(entry.getValue()));
            }
        }
        NBTTagCompound tag = container.getTagCompound();
        if (tag != null) {
            tag.setInteger(InfinityStorageCellBase.NBT_TYPES, gasTypes);
            tag.setString(InfinityStorageCellBase.NBT_BYTES, totalBytes.toString());
        }
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEGasStack iaeGasStack) {
        return false;
    }

    @Override
    public boolean canAccept(IAEGasStack iaeGasStack) {
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
