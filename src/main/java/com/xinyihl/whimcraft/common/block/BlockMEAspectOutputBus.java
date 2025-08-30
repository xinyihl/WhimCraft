package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.common.block.base.BlockMEBase;
import com.xinyihl.whimcraft.common.title.TitleMEAspectOutputBus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.lib.SoundsTC;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMEAspectOutputBus extends BlockMEBase {

    public BlockMEAspectOutputBus() {
        super("blockmeaspectoutputbus");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TitleMEAspectOutputBus();
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState iBlockState, @Nonnull EntityPlayer entityPlayer, @Nonnull EnumHand enumHand, @Nonnull EnumFacing enumFacing, float v, float v1, float v2) {
        TileEntity te = world.getTileEntity(blockPos);
        if (te instanceof TitleMEAspectOutputBus && entityPlayer.isSneaking() && entityPlayer.getHeldItem(enumHand).isEmpty()) {
            if (world.isRemote) {
                world.playSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, SoundsTC.jar, SoundCategory.BLOCKS, 0.4F, 1.0F, false);
                world.playSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.5F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F, false);
            } else {
                ((TitleMEAspectOutputBus) te).spillAll();
            }
            return true;
        }
        return super.onBlockActivated(world, blockPos, iBlockState, entityPlayer, enumHand, enumFacing, v, v1, v2);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TitleMEAspectOutputBus.class;
    }
}
