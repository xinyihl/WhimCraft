package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.common.tile.TileMEPullBus;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerMEPullBus extends Container {

    private final TileMEPullBus tile;

    public ContainerMEPullBus(InventoryPlayer playerInv, TileMEPullBus tile) {
        this.tile = tile;
        IOInventory inv = tile.getInternalInventory();

        // 16 slots: 2 rows x 8 columns
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = col + row * 9;
                addSlotToContainer(new ViewOnlySlot(inv, slot, 8 + col * 18, 18 + row * 18));
            }
        }

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 68 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 126));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return tile != null && !tile.isInvalid() && playerIn.getDistanceSq(tile.getPos()) <= 64.0D;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        // Disallow all shift-click transfers
        return ItemStack.EMPTY;
    }

    public void onAction(String type, NBTTagCompound compound) {
        if ("return_items".equals(type)) {
            tile.returnItemsToAE();
        }
    }

    private static final class ViewOnlySlot extends SlotItemHandler {
        private ViewOnlySlot(IOInventory itemHandler, int index, int xPosition, int yPosition) {
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
