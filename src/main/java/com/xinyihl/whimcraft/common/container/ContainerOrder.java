package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.network.PacketClientToServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerOrder extends Container {

    private final EntityPlayer player;

    public ContainerOrder(EntityPlayer player) {
        this.player = player;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId >= 0 && slotId < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack clickedStack = slot.getStack();
            ItemStack order = player.getHeldItemMainhand();
            if (!clickedStack.isEmpty() && !(clickedStack.getItem() instanceof Order) && order.getItem() instanceof Order) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("type", "click");
                tag.setInteger("slotId", slotId);
                WhimCraft.instance.networkWrapper.sendToServer(new PacketClientToServer(PacketClientToServer.ClientToServer.CLICK_ACTION, tag));
                return clickedStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void onAction(String type, NBTTagCompound compound) {
        int slotId = compound.getInteger("slotId");
        if (slotId >= 0 && slotId < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack clickedStack = slot.getStack();
            ItemStack order = player.getHeldItemMainhand();
            if (!clickedStack.isEmpty() && !(clickedStack.getItem() instanceof Order) && order.getItem() instanceof Order) {
                Order.setMarkedItem(order, clickedStack.copy());
                player.closeScreen();
            }
        }
    }
}
