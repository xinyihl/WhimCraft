package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class GhostItemSlot extends Slot {
    private final EntityPlayer player;

    public GhostItemSlot(EntityPlayer player, int x, int y) {
        super(new InventoryBasic("[Null]", true, 0), 0, x, y);
        this.player = player;
    }

    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }

    public void putStack(@Nonnull ItemStack stack) {
        ItemStack order = player.getHeldItemMainhand();
        if (!stack.isEmpty() && !(stack.getItem() instanceof Order) && order.getItem() instanceof Order) {
            Utils.sendAction("order_set_marked", stack.writeToNBT(new NBTTagCompound()));
        }
    }
}