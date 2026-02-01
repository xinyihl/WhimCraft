package com.xinyihl.whimcraft.common.items.cell.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageEssentiaCell;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityStorageEssentiaCellInventory;
import net.minecraft.item.ItemStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

public class InfinityStorageEssentiaCellHandler implements ICellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityStorageEssentiaCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityStorageEssentiaCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityStorageEssentiaCellInventory(itemStack, iSaveProvider), iStorageChannel);
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
