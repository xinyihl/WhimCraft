package com.xinyihl.whimcraft.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class MyItemBlock extends ItemBlock {
    public MyItemBlock(Block block) {
        super(block);
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        this.setTranslationKey(block.getTranslationKey());
    }

    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if (stack.isEmpty()) {
            return 0;
        }
        try {
            Class<?> configClass = Class.forName("hellfirepvp.modularmachinery.common.data.Config");
            return configClass.getField("machineColor").getInt(null);
        } catch (Throwable ignored) {
            return 0;
        }
    }
}
