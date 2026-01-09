package com.xinyihl.whimcraft.common.handler;

import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.xinyihl.whimcraft.common.inventory.InfinityListFluidCellInventory;
import com.xinyihl.whimcraft.common.inventory.InfinityListItemCellInventory;
import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.item.ItemStack;

public class InfinityListCellHandler implements ICellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() == IB.itemInfinityListCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        InfinityListItemCellInventory itemCell = new InfinityListItemCellInventory(itemStack);
        if (itemCell.getChannel() == iStorageChannel) {
            return new BasicCellInventoryHandler(itemCell, iStorageChannel);
        }
        InfinityListFluidCellInventory fluidCell = new InfinityListFluidCellInventory(itemStack);
        if (fluidCell.getChannel() == iStorageChannel) {
            return new BasicCellInventoryHandler(fluidCell, iStorageChannel);
        }
        return null;
    }

    @Override
    public int getStatusForCell(ItemStack itemStack, ICellInventoryHandler iCellInventoryHandler) {
        return 2;
    }

    @Override
    public double cellIdleDrain(ItemStack itemStack, ICellInventoryHandler iCellInventoryHandler) {
        return 16.0D;
    }
}
