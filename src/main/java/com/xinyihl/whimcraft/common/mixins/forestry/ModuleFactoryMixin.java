package com.xinyihl.whimcraft.common.mixins.forestry;

import forestry.factory.ModuleFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Blaze Tubes rework - Increase speed
 */
@Mixin(value = ModuleFactory.class, remap = false)
public abstract class ModuleFactoryMixin {

    @ModifyConstant(method = "doInit", constant = @Constant(doubleValue = 0.125))
    private double increaseSpeedEmeraldTube(double value) {
        return 1.0;
    }

    @ModifyConstant(method = "doInit", constant = @Constant(doubleValue = 0.25))
    private double increaseSpeedBlazeTube(double value) {
        return 5.0;
    }
}
