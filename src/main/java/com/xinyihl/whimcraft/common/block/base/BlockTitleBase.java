package com.xinyihl.whimcraft.common.block.base;

import com.xinyihl.whimcraft.Tags;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;
import net.minecraft.tileentity.TileEntity;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public abstract class BlockTitleBase extends BlockMEMachineComponent implements IHasTileEntityClass {
    public BlockTitleBase(String name) {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(Tags.MOD_ID, name);
        this.setTranslationKey(Tags.MOD_ID + "." + name);
    }

    abstract public Class<? extends TileEntity> getTileEntityClass();
}
