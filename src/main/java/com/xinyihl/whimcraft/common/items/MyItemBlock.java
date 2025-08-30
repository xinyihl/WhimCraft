package com.xinyihl.whimcraft.common.items;

import hellfirepvp.modularmachinery.common.data.Config;
import hellfirepvp.modularmachinery.common.item.ItemBlockMachineComponent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class MyItemBlock extends ItemBlockMachineComponent {
    public MyItemBlock(Block block) {
        super(block);
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        this.setTranslationKey(block.getTranslationKey());
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        return stack.isEmpty() ? 0 : Config.machineColor;
    }
}
