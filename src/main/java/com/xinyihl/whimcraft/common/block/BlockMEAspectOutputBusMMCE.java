package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.common.block.base.BlockMEBase;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.TitleMEAspectOutputBusMMCE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMEAspectOutputBusMMCE extends BlockMEBase {
    public BlockMEAspectOutputBusMMCE() {
        super("blockmeaspectoutputbusmmce");
        IB.blocks.put(TitleMEAspectOutputBusMMCE.class, this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TitleMEAspectOutputBusMMCE();
    }
}
