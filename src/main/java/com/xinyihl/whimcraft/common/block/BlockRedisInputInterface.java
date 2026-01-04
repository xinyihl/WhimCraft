package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.block.base.IHasTileEntityClass;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import com.xinyihl.whimcraft.common.tile.TileRedisInputInterface;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
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
