package com.xinyihl.whimcraft.common.inventory;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import com.xinyihl.whimcraft.common.items.InfinityListCell;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfinityListItemCellInventory implements IMEInventoryHandler<IAEItemStack> {
    private final List<IAEItemStack> records = new ArrayList<>();

    public InfinityListItemCellInventory(ItemStack stack) {
        for (Object obj : InfinityListCell.getRecords(stack)) {
            if (obj instanceof IAEItemStack) {
                IAEItemStack s = (IAEItemStack) obj;
                s.setStackSize(2147483647L);
                records.add(s);
            }
        }
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEItemStack record : records) {
            if (record.equals(stack)) return null;
        }
        return stack;
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack stack, Actionable actionable, IActionSource iActionSource) {
        for (IAEItemStack record : records) {
            if (record.equals(stack)) return stack.copy();
        }
        return null;
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> iItemList) {
        for (IAEItemStack record : records) {
            iItemList.add(record);
        }
        return iItemList;
    }

    @Override
    public IStorageChannel<IAEItemStack> getChannel() {
        return AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEItemStack stack) {
        for (IAEItemStack record : records) {
            if (record.equals(stack)) return true;
        }
        return false;
    }

    @Override
    public boolean canAccept(IAEItemStack stack) {
        for (IAEItemStack record : records) {
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
