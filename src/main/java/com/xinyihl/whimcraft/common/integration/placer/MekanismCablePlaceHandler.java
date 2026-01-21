package com.xinyihl.whimcraft.common.integration.placer;

import com.xinyihl.whimcraft.api.ICablePlacer;
import com.xinyihl.whimcraft.common.items.placer.CablePlaceContext;
import mekanism.common.block.BlockTransmitter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

public class MekanismCablePlaceHandler implements ICablePlacer {

    @Override
    public boolean canSelect(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof ItemBlock)) return false;
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        return block instanceof BlockTransmitter;
    }

    @Override
    @Optional.Method(modid = "mekanism")
    public boolean placeCable(CablePlaceContext ctx) {
        World world = ctx.world;
        BlockPos target = ctx.target;
        if (!ctx.allowReplace && !world.isAirBlock(target)) {
            return false;
        }
        if (!world.isAirBlock(target) && !world.getBlockState(target).getMaterial().isReplaceable()) {
            return false;
        }
        if (!(ctx.cableStack.getItem() instanceof ItemBlock)) {
            return false;
        }
        Block block = ((ItemBlock) ctx.cableStack.getItem()).getBlock();
        if (!(block instanceof BlockTransmitter)) {
            return false;
        }
        return block.canPlaceBlockAt(world, target) && ((ItemBlock) ctx.cableStack.getItem()).placeBlockAt(ctx.cableStack, ctx.player, world, target, EnumFacing.DOWN, 0, 0, 0, block.getStateFromMeta(ctx.cableStack.getItemDamage()));
    }
}
