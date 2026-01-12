package com.xinyihl.whimcraft.common.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import com.xinyihl.whimcraft.common.inventory.InfinityStorageGasCellInventory;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageGasCell;
import net.minecraft.item.ItemStack;

public class InfinityStorageGasCellHandler implements ICellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityStorageGasCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityStorageGasCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityStorageGasCellInventory(itemStack, iSaveProvider), iStorageChannel);
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
