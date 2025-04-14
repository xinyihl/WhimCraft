package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.Tags;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class BlockShareInfHandler extends BlockMEMachineComponent {
    public BlockShareInfHandler() {
        super();
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(Tags.MOD_ID, "blockshareinfhandler");
        this.setTranslationKey(Tags.MOD_ID + ".blockshareinfhandler");
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState iBlockState, @Nonnull EntityPlayer entityPlayer, @Nonnull EnumHand enumHand, @Nonnull EnumFacing enumFacing, float v, float v1, float v2) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TitleShareInfHandler();
    }
}
