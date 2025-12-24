package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.network.PacketClientToServer;
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
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", "click");
            tag.setTag("nbt", stack.writeToNBT(new NBTTagCompound()));
            WhimCraft.instance.networkWrapper.sendToServer(new PacketClientToServer(PacketClientToServer.ClientToServer.CLICK_ACTION, tag));
        }
    }
}