package com.xinyihl.whimcraft.common.block;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.block.base.IHasTileEntityClass;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import com.xinyihl.whimcraft.common.tile.TileRedisOutputInterface;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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

public class BlockRedisOutputInterface extends BlockContainer implements IHasTileEntityClass {

    public BlockRedisOutputInterface() {
        super(Material.IRON);
        setHardness(2.0F);
        setResistance(10.0F);
        setCreativeTab(CREATIVE_TAB);
        setRegistryName(Tags.MOD_ID, "redis_output_interface");
        setTranslationKey(Tags.MOD_ID + ".redis_output_interface");
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.getHeldItemMainhand().isEmpty()) {
            player.openGui(WhimCraft.instance, GuiHandler.REDIS_OUTPUT_GUI, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileRedisOutputInterface();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileRedisOutputInterface.class;
    }
}
