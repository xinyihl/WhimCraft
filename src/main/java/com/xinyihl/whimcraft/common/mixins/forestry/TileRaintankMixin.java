package com.xinyihl.whimcraft.common.mixins.forestry;

import forestry.factory.tiles.TileRaintank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Buff Rain Tank: increase capacity, fluid per operation, dump speed and decrease interval.
 */
@Mixin(value = TileRaintank.class, remap = false)
public abstract class TileRaintankMixin {

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 10))
    private static int increaseFluidPerOperation(int value) {
        return 500000;
    }

    @ModifyConstant(method = "dumpFluidBelow", constant = @Constant(intValue = 50))
    private int dumpBelowPerOperation(int value) {
        return 50000;
    }

    @ModifyConstant(method = "updateServerSide", constant = @Constant(intValue = 20))
    private int decreaseInterval(int value) {
        return 5;
    }

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 30000))
    private int increaseCapacity(int value) {
        return 500000000;
    }
}
