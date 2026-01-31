package com.xinyihl.whimcraft.common.items.cell.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IItemList;
import com.mekeng.github.common.me.data.IAEGasStack;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import com.xinyihl.whimcraft.common.items.cell.InfinityListGasCell;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfinityListGasCellInventory implements IMEInventoryHandler<IAEGasStack> {
    private final List<IAEGasStack> records = new ArrayList<>();

    public InfinityListGasCellInventory(ItemStack stack) {
        for (IAEGasStack gas : InfinityListGasCell.getRecords(stack)) {
            if (gas == null) continue;
            gas.setStackSize(1000L * Integer.MAX_VALUE);
            records.add(gas);
        }
    }

    @Override
    public IAEGasStack injectItems(IAEGasStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEGasStack record : records) {
            if (record.equals(stack)) return null;
        }
        return stack;
    }

    @Override
    public IAEGasStack extractItems(IAEGasStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEGasStack record : records) {
            if (record.equals(stack)) return stack.copy();
        }
        return null;
    }

    @Override
    public IItemList<IAEGasStack> getAvailableItems(IItemList<IAEGasStack> iItemList) {
        for (IAEGasStack record : records) {
            iItemList.add(record);
        }
        return iItemList;
    }

    @Override
    public IStorageChannel<IAEGasStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEGasStack stack) {
        for (IAEGasStack record : records) {
            if (record.equals(stack)) return true;
        }
        return false;
    }

    @Override
    public boolean canAccept(IAEGasStack stack) {
        for (IAEGasStack record : records) {
            if (record.equals(stack)) return true;
        }
        return false;
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
