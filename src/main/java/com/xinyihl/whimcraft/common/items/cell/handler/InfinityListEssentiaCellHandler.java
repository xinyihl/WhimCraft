package com.xinyihl.whimcraft.common.items.cell.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.xinyihl.whimcraft.api.IInfinityListCellHandler;
import com.xinyihl.whimcraft.common.items.cell.InfinityListEssentiaCell;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityListEssentiaCellInventory;
import net.minecraft.item.ItemStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

public class InfinityListEssentiaCellHandler implements IInfinityListCellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityListEssentiaCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityListEssentiaCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class)) {
            return new BasicCellInventoryHandler(new InfinityListEssentiaCellInventory(itemStack), iStorageChannel);
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
