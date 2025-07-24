package com.xinyihl.whimcraft.common.block.base;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.items.MyItemBlock;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;
import net.minecraft.tileentity.TileEntity;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public abstract class BlockTitleBase extends BlockMEMachineComponent {
    public BlockTitleBase(Class<? extends TileEntity> clzz, String name) {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(Tags.MOD_ID, name);
        this.setTranslationKey(Tags.MOD_ID + "." + name);
        IB.blocks.put(clzz, this);
        new MyItemBlock(this);
    }
}
