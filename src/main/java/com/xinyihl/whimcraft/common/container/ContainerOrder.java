package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.api.IActionHandler;
import com.xinyihl.whimcraft.client.GhostItemSlot;
import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerOrder extends Container implements IActionHandler {

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
        this.addSlotToContainer(new GhostItemSlot(player, 80, 35));
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
                Utils.sendAction("order_set_marked", clickedStack.writeToNBT(new NBTTagCompound()));
                return clickedStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onAction(String type, NBTTagCompound compound) {
        if ("order_set_marked".equals(type)) {
            ItemStack clickedStack = new ItemStack(compound);
            ItemStack order = player.getHeldItemMainhand();
            if (!clickedStack.isEmpty() && !(clickedStack.getItem() instanceof Order) && order.getItem() instanceof Order) {
                Order.setMarkedItem(order, clickedStack.copy());
                player.closeScreen();
            }
        }
    }
}
