package com.xinyihl.whimcraft.common.integration.placer;

import appeng.api.AEApi;
import appeng.items.parts.ItemPart;
import appeng.items.parts.PartType;
import com.xinyihl.whimcraft.api.ICablePlacer;
import com.xinyihl.whimcraft.common.items.placer.CablePlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

public class Ae2CablePlaceHandler implements ICablePlacer {

    @Override
    public boolean canSelect(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof ItemPart)) return false;
        PartType type = ((ItemPart) stack.getItem()).getTypeByStack(stack);
        return type.isCable() && type != PartType.CABLE_ANCHOR;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean placeCable(CablePlaceContext ctx) {
        World world = ctx.world;
        BlockPos target = ctx.target;
        if (!ctx.allowReplace && !world.isAirBlock(target)) {
            return false;
        }
        for (EnumFacing faceFromAnchorToTarget : EnumFacing.values()) {
            BlockPos anchorPos = target.offset(faceFromAnchorToTarget.getOpposite());
            if (world.isAirBlock(anchorPos)) {
                continue;
            }
            EnumActionResult r = AEApi.instance().partHelper().placeBus(ctx.cableStack, anchorPos, faceFromAnchorToTarget, ctx.player, ctx.hand, world);
            if (r == EnumActionResult.SUCCESS) {
                return true;
            }
        }
        return false;
    }
}
