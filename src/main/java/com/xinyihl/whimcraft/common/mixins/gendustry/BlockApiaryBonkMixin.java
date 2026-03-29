package com.xinyihl.whimcraft.common.mixins.gendustry;

import net.bdew.gendustry.machines.apiary.BlockApiary$;
import net.bdew.gendustry.machines.apiary.TileApiary;
import net.bdew.lib.block.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(value = BlockApiary$.class, remap = false)
public abstract class BlockApiaryBonkMixin extends BaseBlock {

    public BlockApiaryBonkMixin(String name, Material material) {
        super(name, material);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        ItemStack heldItem = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        if (!worldIn.isRemote && !heldItem.isEmpty()) {
            if (heldItem.getItem() == Items.STICK) {
                TileEntity tile = worldIn.getTileEntity(pos);
                if (tile instanceof TileApiary) {
                    ((TileApiary) tile).getBeekeepingLogic().clearCachedValues();
                }
            }
        }
    }
}