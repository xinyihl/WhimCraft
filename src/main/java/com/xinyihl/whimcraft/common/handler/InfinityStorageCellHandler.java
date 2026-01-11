package com.xinyihl.whimcraft.common.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.xinyihl.whimcraft.common.inventory.InfinityStorageFluidCellInventory;
import com.xinyihl.whimcraft.common.inventory.InfinityStorageItemCellInventory;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageFluidCell;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageItemCell;
import net.minecraft.item.ItemStack;

public class InfinityStorageCellHandler implements ICellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityStorageItemCell || itemStack.getItem() instanceof InfinityStorageFluidCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityStorageItemCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityStorageItemCellInventory(itemStack, iSaveProvider), iStorageChannel);
        }
        if (itemStack.getItem() instanceof InfinityStorageFluidCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityStorageFluidCellInventory(itemStack, iSaveProvider), iStorageChannel);
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
