package com.xinyihl.whimcraft.common.blocks.base;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.api.IHasTileEntityClass;
import hellfirepvp.modularmachinery.common.block.BlockMachineComponent;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public abstract class BlockTileBase extends BlockMachineComponent implements IHasTileEntityClass {
    public BlockTileBase(String name) {
        super(Material.IRON);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 1);
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(Tags.MOD_ID, name);
        this.setTranslationKey(Tags.MOD_ID + "." + name);
    }
}
