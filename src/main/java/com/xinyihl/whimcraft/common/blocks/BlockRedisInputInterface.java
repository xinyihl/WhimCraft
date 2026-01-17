package com.xinyihl.whimcraft.common.blocks;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.api.IHasTileEntityClass;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import com.xinyihl.whimcraft.common.tile.TileRedisInputInterface;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class BlockRedisInputInterface extends BlockContainer implements IHasTileEntityClass {

    public BlockRedisInputInterface() {
        super(Material.IRON);
        setHardness(2.0F);
        setResistance(10.0F);
        setCreativeTab(CREATIVE_TAB);
        setRegistryName(Tags.MOD_ID, "redis_input_interface");
        setTranslationKey(Tags.MOD_ID + ".redis_input_interface");
    }

    @Nonnull
    public EnumBlockRenderType getRenderType(@Nonnull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        for (int i = 0; i < 3; ++i) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = pos.getX() + 0.5D + 0.25D * j;
            double d1 = pos.getY() + rand.nextDouble();
            double d2 = pos.getZ() + 0.5D + 0.25D * k;
            double d3 = rand.nextDouble() * j;
            double d4 = (rand.nextDouble() - 0.5D) * 0.125D;
            double d5 = rand.nextDouble() * k;
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.getHeldItemMainhand().isEmpty()) {
            player.openGui(WhimCraft.instance, GuiHandler.REDIS_INPUT_GUI, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileRedisInputInterface();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileRedisInputInterface.class;
    }
}
