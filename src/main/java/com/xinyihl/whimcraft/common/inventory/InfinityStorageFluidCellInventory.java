package com.xinyihl.whimcraft.common.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageFluidCell;
import com.xinyihl.whimcraft.common.storage.InfinityStorageWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import java.util.Map;
import java.util.UUID;

public class InfinityStorageFluidCellInventory implements IMEInventoryHandler<IAEFluidStack> {
    private final UUID uuid;
    private final ItemStack container;
    private ISaveProvider saveProvider;

    public InfinityStorageFluidCellInventory(ItemStack container, ISaveProvider saveProvider) {
        this.uuid = InfinityStorageFluidCell.getOrCreateUuid(container);
        this.container = container;
        this.saveProvider = saveProvider;
    }

    private InfinityStorageWorldData getWorldData() {
        return InfinityStorageWorldData.get(DimensionManager.getWorld(0));
    }

    private IAEStack findMatchingStack(IAEFluidStack stack) {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        for (IAEStack stored : storage.keySet()) {
            if (stored instanceof IAEFluidStack && stored.equals(stack)) {
                return stored;
            }
        }
        return null;
    }

    @Override
    public IAEFluidStack injectItems(IAEFluidStack input, Actionable actionable, IActionSource src) {
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
    public IAEFluidStack extractItems(IAEFluidStack request, Actionable actionable, IActionSource src) {
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
        IAEFluidStack result = (IAEFluidStack) existingKey.copy();
        result.setStackSize(extractAmount);
        return result;
    }

    @Override
    public IItemList<IAEFluidStack> getAvailableItems(IItemList<IAEFluidStack> out) {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            IAEStack stack = entry.getKey();
            if (stack instanceof IAEFluidStack) {
                IAEFluidStack fluidStack = (IAEFluidStack) stack.copy();
                fluidStack.setStackSize(entry.getValue());
                out.add(fluidStack);
            }
        }
        return out;
    }

    @Override
    public IStorageChannel<IAEFluidStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class);
    }

    private void updateStats() {
        Map<IAEStack, Long> storage = getWorldData().getStorage(uuid);
        int fluidTypes = 0;
        long totalBytes = 0;
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            fluidTypes++;
            totalBytes += entry.getValue();
        }
        NBTTagCompound tag = container.getTagCompound();
        if (tag != null) {
            tag.setInteger(InfinityStorageFluidCell.NBT_FLUID_TYPES, fluidTypes);
            tag.setLong(InfinityStorageFluidCell.NBT_DATA_BYTES, totalBytes);
        }
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEFluidStack iaeFluidStack) {
        return false;
    }

    @Override
    public boolean canAccept(IAEFluidStack iaeFluidStack) {
        return true; // 无限存储可以接受任何流体
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
        return true; // 对所有 pass 有效
    }
}
