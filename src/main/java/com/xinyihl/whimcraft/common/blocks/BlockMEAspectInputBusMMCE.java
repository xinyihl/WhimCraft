package com.xinyihl.whimcraft.common.blocks;

import com.xinyihl.whimcraft.common.blocks.base.BlockMEBase;
import com.xinyihl.whimcraft.common.tile.TileMEAspectInputBusMMCE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMEAspectInputBusMMCE extends BlockMEBase {
    public BlockMEAspectInputBusMMCE() {
        super("blockmeaspectinputbusmmce");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TileMEAspectInputBusMMCE();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileMEAspectInputBusMMCE.class;
    }
}
