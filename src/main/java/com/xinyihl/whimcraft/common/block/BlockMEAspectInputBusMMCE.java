package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.common.block.base.BlockMEBase;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.TitleMEAspectInputBusMMCE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMEAspectInputBusMMCE extends BlockMEBase {
    public BlockMEAspectInputBusMMCE() {
        super("blockmeaspectinputbusmmce");
        IB.blocks.put(TitleMEAspectInputBusMMCE.class, this);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TitleMEAspectInputBusMMCE();
    }
}
