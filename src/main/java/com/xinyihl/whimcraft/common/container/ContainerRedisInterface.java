package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.common.tile.base.TileRedisInterfaceBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerRedisInterface extends Container {

    private final TileRedisInterfaceBase tile;
    private final boolean isInput;

    public ContainerRedisInterface(InventoryPlayer playerInv, TileRedisInterfaceBase tile, boolean isInput) {
        this.tile = tile;
        this.isInput = isInput;
        ItemStackHandler inv = tile.getInventory();
        int rows = TileRedisInterfaceBase.ROWS;
        int yBase = (rows - 4) * 18;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < TileRedisInterfaceBase.COLUMNS; col++) {
                int slot = col + row * TileRedisInterfaceBase.COLUMNS;
                int x = 8 + col * 18;
                int y = 18 + row * 18;
                if (isInput) {
                    addSlotToContainer(new SlotInputOnly(inv, slot, x, y));
                } else {
                    addSlotToContainer(new SlotOutputOnly(inv, slot, x, y));
                }
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + yBase));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 161 + yBase));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return tile != null && !tile.isInvalid() && playerIn.getDistanceSq(tile.getPos()) <= 64.0D;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        int tileSize = TileRedisInterfaceBase.SIZE;
        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemstack = stackInSlot.copy();
            if (index < tileSize) {
                if (!this.mergeItemStack(stackInSlot, tileSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!isInput) {
                    return ItemStack.EMPTY;
                }
                if (!this.mergeItemStack(stackInSlot, 0, tileSize, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stackInSlot);
        }
        return itemstack;
    }

    private static final class SlotInputOnly extends SlotItemHandler {
        private SlotInputOnly(ItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            return false;
        }
    }

    private static final class SlotOutputOnly extends SlotItemHandler {
        private SlotOutputOnly(ItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
