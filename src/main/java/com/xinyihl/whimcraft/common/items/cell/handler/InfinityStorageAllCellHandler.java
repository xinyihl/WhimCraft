package com.xinyihl.whimcraft.common.items.cell.handler;

import appeng.api.AEApi;
import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.BasicCellInventoryHandler;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.items.cell.InfinityStorageAllCell;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityStorageEssentiaCellInventory;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityStorageFluidCellInventory;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityStorageGasCellInventory;
import com.xinyihl.whimcraft.common.items.cell.inventory.InfinityStorageItemCellInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

public class InfinityStorageAllCellHandler implements ICellHandler {
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof InfinityStorageAllCell;
    }

    @Override
    public <T extends IAEStack<T>> ICellInventoryHandler<T> getCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        ICellInventoryHandler<T> handler = null;
        if (itemStack.getItem() instanceof InfinityStorageAllCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class)) {
            handler = new BasicCellInventoryHandler<>(new InfinityStorageItemCellInventory(itemStack, iSaveProvider), iStorageChannel);
        }
        if (itemStack.getItem() instanceof InfinityStorageAllCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class)) {
            handler = new BasicCellInventoryHandler<>(new InfinityStorageFluidCellInventory(itemStack, iSaveProvider), iStorageChannel);
        }
        if (handler == null && Mods.MEKENG.isLoaded()) {
            handler = this.getGasCellInventory(itemStack, iSaveProvider, iStorageChannel);
        }
        if (handler == null && Mods.TCENERG.isLoaded()) {
            handler = this.getEssCellInventory(itemStack, iSaveProvider, iStorageChannel);
        }
        return handler;
    }

    @Optional.Method(modid = "mekeng")
    private <T extends IAEStack<T>> ICellInventoryHandler<T> getGasCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityStorageAllCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class)) {
            return new BasicCellInventoryHandler<>(new InfinityStorageGasCellInventory(itemStack, iSaveProvider), iStorageChannel);
        }
        return null;
    }

    @Optional.Method(modid = "thaumicenergistics")
    private <T extends IAEStack<T>> ICellInventoryHandler<T> getEssCellInventory(ItemStack itemStack, ISaveProvider iSaveProvider, IStorageChannel<T> iStorageChannel) {
        if (itemStack.getItem() instanceof InfinityStorageAllCell && iStorageChannel == AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class)) {
            return new BasicCellInventoryHandler<>(new InfinityStorageEssentiaCellInventory(itemStack, iSaveProvider), iStorageChannel);
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
