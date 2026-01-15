package com.xinyihl.whimcraft.common.items.cell.inventory;

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
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;
import com.xinyihl.whimcraft.common.storage.InfinityStorageWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import java.math.BigInteger;
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

    @Override
    public IAEFluidStack injectItems(IAEFluidStack input, Actionable actionable, IActionSource src) {
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
    public IAEFluidStack extractItems(IAEFluidStack request, Actionable actionable, IActionSource src) {
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
        IAEFluidStack result = (IAEFluidStack) key.copy();
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
        BigInteger totalBytes = BigInteger.ZERO;
        for (Map.Entry<IAEStack, Long> entry : storage.entrySet()) {
            fluidTypes++;
            totalBytes = totalBytes.add(BigInteger.valueOf(entry.getValue()));
        }
        NBTTagCompound tag = container.getTagCompound();
        if (tag != null) {
            tag.setInteger(InfinityStorageCellBase.NBT_TYPES, fluidTypes);
            tag.setString(InfinityStorageCellBase.NBT_BYTES, totalBytes.toString());
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
