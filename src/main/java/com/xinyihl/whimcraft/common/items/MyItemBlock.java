package com.xinyihl.whimcraft.common.items;

import hellfirepvp.modularmachinery.common.data.Config;
import hellfirepvp.modularmachinery.common.item.ItemDynamicColor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.util.Objects;

@Optional.Interface(modid = "modularmachinery", iface = "hellfirepvp.modularmachinery.common.item.ItemDynamicColor")
public class MyItemBlock extends ItemBlock implements ItemDynamicColor {
    public MyItemBlock(Block block) {
        super(block);
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        this.setTranslationKey(block.getTranslationKey());
    }

    @Override
    @Optional.Method(modid = "modularmachinery")
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        return stack.isEmpty() ? 0 : Config.machineColor;
    }
}
