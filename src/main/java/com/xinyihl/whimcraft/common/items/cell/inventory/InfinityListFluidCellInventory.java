package com.xinyihl.whimcraft.common.items.cell.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.cell.InfinityListFluidCell;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfinityListFluidCellInventory implements IMEInventoryHandler<IAEFluidStack> {
    private final List<IAEFluidStack> records = new ArrayList<>();

    public InfinityListFluidCellInventory(ItemStack stack) {
        for (IAEFluidStack fluid : InfinityListFluidCell.getRecords(stack)) {
            fluid.setStackSize(2147483647000L);
            records.add(fluid);
        }
    }

    @Override
    public IAEFluidStack injectItems(IAEFluidStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEFluidStack record : records) {
            if (record.equals(stack)) return null;
        }
        return stack;
    }

    @Override
    public IAEFluidStack extractItems(IAEFluidStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEFluidStack record : records) {
            if (record.equals(stack)) return stack.copy();
        }
        return null;
    }

    @Override
    public IItemList<IAEFluidStack> getAvailableItems(IItemList<IAEFluidStack> iItemList) {
        for (IAEFluidStack record : records) {
            iItemList.add(record);
        }
        return iItemList;
    }

    @Override
    public IStorageChannel<IAEFluidStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEFluidStack stack) {
        for (IAEFluidStack record : records) {
            if (record.equals(stack)) return true;
        }
        return false;
    }

    @Override
    public boolean canAccept(IAEFluidStack stack) {
        for (IAEFluidStack record : records) {
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
