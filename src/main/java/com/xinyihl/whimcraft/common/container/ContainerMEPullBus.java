package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.api.IActionHandler;
import com.xinyihl.whimcraft.common.tile.TileMEPullBus;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerMEPullBus extends Container implements IActionHandler {

    private final TileMEPullBus tile;

    public ContainerMEPullBus(InventoryPlayer playerInv, TileMEPullBus tile) {
        this.tile = tile;
        IOInventory inv = tile.getInternalInventory();

        // Left 4x4 grid: internal inventory slots
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int slot = row * 4 + col;
                addSlotToContainer(new ViewOnlySlot(inv, slot, 98 + col * 18, 35 + row * 18));
            }
        }

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 123 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 181));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return tile != null && !tile.isInvalid() && playerIn.getDistanceSq(tile.getPos()) <= 64.0D;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onAction(String type, NBTTagCompound compound) {
        if ("return_items".equals(type)) {
            tile.returnItemsToAE();
        }
    }

    private static final class ViewOnlySlot extends SlotItemHandler {
        private ViewOnlySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            return false;
        }
    }
}
