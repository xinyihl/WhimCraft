package com.xinyihl.whimcraft.common.items.cell.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.cell.InfinityListEssentiaCell;
import net.minecraft.item.ItemStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

import java.util.ArrayList;
import java.util.List;

public class InfinityListEssentiaCellInventory implements IMEInventoryHandler<IAEEssentiaStack> {

    private final List<IAEEssentiaStack> records = new ArrayList<>();

    public InfinityListEssentiaCellInventory(ItemStack stack) {
        for (IAEEssentiaStack rec : InfinityListEssentiaCell.getRecords(stack)) {
            if (rec == null) continue;
            IAEEssentiaStack copy = rec.copy();
            copy.setStackSize(Integer.MAX_VALUE);
            this.records.add(copy);
        }
    }

    @Override
    public IAEEssentiaStack injectItems(IAEEssentiaStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEEssentiaStack record : records) {
            if (record.equals(stack)) return null;
        }
        return stack;
    }

    @Override
    public IAEEssentiaStack extractItems(IAEEssentiaStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEEssentiaStack record : records) {
            if (record.equals(stack)) return stack.copy();
        }
        return null;
    }

    @Override
    public IItemList<IAEEssentiaStack> getAvailableItems(IItemList<IAEEssentiaStack> iItemList) {
        for (IAEEssentiaStack record : records) {
            iItemList.add(record);
        }
        return iItemList;
    }

    @Override
    public IStorageChannel<IAEEssentiaStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEEssentiaStack stack) {
        for (IAEEssentiaStack record : records) {
            if (record.equals(stack)) return true;
        }
        return false;
    }

    @Override
    public boolean canAccept(IAEEssentiaStack stack) {
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
