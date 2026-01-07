package com.xinyihl.whimcraft.common.block.base;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.api.IHasTileEntityClass;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public abstract class BlockTileBase extends BlockMEMachineComponent implements IHasTileEntityClass {
    public BlockTileBase(String name) {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(Tags.MOD_ID, name);
        this.setTranslationKey(Tags.MOD_ID + "." + name);
    }
}
