package com.xinyihl.whimcraft.common.integration.jei;

import com.xinyihl.whimcraft.client.GhostItemSlot;
import com.xinyihl.whimcraft.client.OrderGui;
import mezz.jei.api.gui.IGhostIngredientHandler;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GhostJEIHandler implements IGhostIngredientHandler<OrderGui> {
    @Override
    public <I> List<Target<I>> getTargets(OrderGui gui, I ingredient, boolean doStart) {
        List<IGhostIngredientHandler.Target<I>> targets = new ArrayList<>();
        if (ingredient instanceof ItemStack) {
            ItemStack stack = ((ItemStack) ingredient).copy();

            for (Slot slot : gui.inventorySlots.inventorySlots) {
                if (slot instanceof GhostItemSlot && slot.isEnabled() && slot.isItemValid(stack)) {
                    targets.add(new GhostTarget<>(slot, gui.getGuiLeft(), gui.getGuiTop()));
                }
            }
        }

        return targets;
    }

    @Override
    public void onComplete() {

    }

    private static class GhostTarget<I> implements IGhostIngredientHandler.Target<I> {
        private final Rectangle rectangle;
        private final Slot slot;

        public GhostTarget(Slot slot, int xoff, int yoff) {
            this.rectangle = new Rectangle(slot.xPos + xoff, slot.yPos + yoff, 16, 16);
            this.slot = slot;
        }

        public Rectangle getArea() {
            return this.rectangle;
        }

        public void accept(I ingredient) {
            if (ingredient instanceof ItemStack) {
                ItemStack stack = ((ItemStack) ingredient).copy();
                this.slot.putStack(stack);
            }
        }
    }
}
