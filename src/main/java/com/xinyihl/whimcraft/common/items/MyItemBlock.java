package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public class MyItemBlock extends ItemBlock {
    public MyItemBlock(Block block) {
        super(block);
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        this.setTranslationKey(block.getTranslationKey());
        IB.items.add(this);
    }
}
