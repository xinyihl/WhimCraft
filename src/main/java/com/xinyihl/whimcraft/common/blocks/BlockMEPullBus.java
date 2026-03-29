package com.xinyihl.whimcraft.common.blocks;

import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.blocks.base.BlockMEBase;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import com.xinyihl.whimcraft.common.tile.TileMEPullBus;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMEPullBus extends BlockMEBase {

    public BlockMEPullBus() {
        super("blockmepullbus");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TileMEPullBus();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileMEPullBus.class;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(WhimCraft.instance, GuiHandler.ME_PULL_BUS_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileMEPullBus && placer instanceof EntityPlayer) {
                TileMEPullBus tile = (TileMEPullBus) te;
                tile.getProxy().setOwner((EntityPlayer) placer);
                tile.getActionableNode();
                NBTTagCompound tag = stack.getTagCompound();
                if (tag != null && tag.hasKey("inventory")) {
                    tile.setInventory(IOInventory.deserialize(tile, tag.getCompoundTag("inventory")));
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        if (willHarvest) return true; // Delay removal for getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        TileEntity te = world.getTileEntity(pos);
        ItemStack drop = new ItemStack(this);
        if (te instanceof TileMEPullBus) {
            IOInventory inv = ((TileMEPullBus) te).getInternalInventory();
            boolean hasItems = false;
            for (int i = 0; i < inv.getSlots(); i++) {
                if (!inv.getStackInSlot(i).isEmpty()) {
                    hasItems = true;
                    break;
                }
            }
            if (hasItems) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("inventory", inv.writeNBT());
                drop.setTagCompound(tag);
            }
        }
        drops.add(drop);
    }
}
