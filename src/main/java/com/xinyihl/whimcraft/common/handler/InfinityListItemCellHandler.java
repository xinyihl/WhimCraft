package com.xinyihl.whimcraft.common.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.xinyihl.whimcraft.common.api.IInfinityListCellHandler;
import com.xinyihl.whimcraft.common.inventory.InfinityListItemCellInventory;
import com.xinyihl.whimcraft.common.items.cell.InfinityListItemCell;
import net.minecraft.item.ItemStack;

public class InfinityListItemCellHandler implements IInfinityListCellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityListItemCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityListItemCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityListItemCellInventory(itemStack), iStorageChannel);
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
