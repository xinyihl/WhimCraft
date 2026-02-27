package com.xinyihl.whimcraft.common.items.cell;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class InfinityListItemCellRegistry {
    private static final List<ItemStack> JEI_STACKS = new ArrayList<>();

    private InfinityListItemCellRegistry() {
    }

    public static synchronized void registerJeiStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        for (ItemStack exists : JEI_STACKS) {
            if (ItemStack.areItemsEqual(exists, stack) && ItemStack.areItemStackTagsEqual(exists, stack)) {
                return;
            }
        }
        JEI_STACKS.add(stack.copy());
    }

    public static synchronized List<ItemStack> getJeiStacks() {
        List<ItemStack> result = new ArrayList<>(JEI_STACKS.size());
        for (ItemStack stack : JEI_STACKS) {
            result.add(stack.copy());
        }
        return result;
    }
}