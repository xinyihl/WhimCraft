package com.xinyihl.whimcraft.common.blocks;

import com.xinyihl.whimcraft.common.blocks.base.BlockTileBase;
import com.xinyihl.whimcraft.common.tile.TileShareInfHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockShareInfHandler extends BlockTileBase {
    public BlockShareInfHandler() {
        super("blockshareinfhandler");
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState iBlockState, @Nonnull EntityPlayer entityPlayer, @Nonnull EnumHand enumHand, @Nonnull EnumFacing enumFacing, float v, float v1, float v2) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TileShareInfHandler();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileShareInfHandler.class;
    }
}
