package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.common.block.base.BlockMEBase;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.TitleMEAspectOutputBus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMEAspectOutputBus extends BlockMEBase {

    public BlockMEAspectOutputBus() {
        super(TitleMEAspectOutputBus.class, "blockmeaspectoutputbus");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TitleMEAspectOutputBus();
    }
}
