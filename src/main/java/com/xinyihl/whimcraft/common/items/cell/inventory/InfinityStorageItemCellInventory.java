package com.xinyihl.whimcraft.common.items.cell.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageItemCell;
import com.xinyihl.whimcraft.common.storage.InfinityStorageWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import java.util.Map;
import java.util.UUID;

public class InfinityStorageItemCellInventory implements IMEInventoryHandler<IAEItemStack> {
    private final UUID uuid;
    private final ItemStack container;
    private ISaveProvider saveProvider;

    public InfinityStorageItemCellInventory(ItemStack container, ISaveProvider saveProvider) {
        this.uuid = InfinityStorageItemCell.getOrCreateUuid(container);
        this.container = container;
        this.saveProvider = saveProvider;
    }

    private InfinityStorageWorldData getWorldData() {
        return InfinityStorageWorldData.get(DimensionManager.getWorld(0));
    }

    private IAEStack findMatchingStack(IAEItemStack stack) {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        for (IAEStack stored : storage.keySet()) {
            if (stored instanceof IAEItemStack && stored.equals(stack)) {
                return stored;
            }
        }
        return null;
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable actionable, IActionSource src) {
        if (input == null || input.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Map<IAEStack, Long> storage = worldData.getStorage(uuid);
        IAEStack existingKey = findMatchingStack(input);
        if (actionable == Actionable.MODULATE) {
            if (existingKey == null) {
                existingKey = input.copy();
                existingKey.setStackSize(1);
                storage.put(existingKey, input.getStackSize());
                updateStats();
            } else {
                storage.compute(existingKey, (k, currentCount) -> currentCount + input.getStackSize());
            }
            worldData.markDirty();
            if (saveProvider != null) {
                saveProvider.saveChanges(null);
            }
        }
        return null;
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable actionable, IActionSource src) {
        if (request == null || request.getStackSize() <= 0) {
            return null;
        }
        InfinityStorageWorldData worldData = getWorldData();
        Map<IAEStack, Long> storage = worldData.getStorage(uuid);
        IAEStack existingKey = findMatchingStack(request);
        if (existingKey == null) {
            return null;
        }
        long currentCount = storage.get(existingKey);
        long extractAmount = Math.min(currentCount, request.getStackSize());
        if (extractAmount <= 0) {
            return null;
        }
        if (actionable == Actionable.MODULATE) {
            long remaining = currentCount - extractAmount;
            if (remaining <= 0) {
                storage.remove(existingKey);
                updateStats();
            } else {
                storage.put(existingKey, remaining);
            }
            worldData.markDirty();
            if (saveProvider != null) {
                saveProvider.saveChanges(null);
            }
        }
        IAEItemStack result = (IAEItemStack) existingKey.copy();
        result.setStackSize(extractAmount);
        return result;
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out) {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            IAEStack stack = entry.getKey();
            if (stack instanceof IAEItemStack) {
                IAEItemStack itemStack = (IAEItemStack) stack.copy();
                itemStack.setStackSize(entry.getValue());
                out.add(itemStack);
            }
        }
        return out;
    }

    @Override
    public IStorageChannel<IAEItemStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
    }

    private void updateStats() {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        int itemTypes = 0;
        long totalBytes = 0;
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            itemTypes++;
            totalBytes += entry.getValue();
        }
        NBTTagCompound tag = container.getTagCompound();
        if (tag != null) {
            tag.setInteger(InfinityStorageItemCell.NBT_ITEM_TYPES, itemTypes);
            tag.setLong(InfinityStorageItemCell.NBT_DATA_BYTES, totalBytes);
        }
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEItemStack iaeItemStack) {
        return false;
    }

    @Override
    public boolean canAccept(IAEItemStack iaeItemStack) {
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
