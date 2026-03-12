package com.xinyihl.whimcraft.common.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import vazkii.botania.common.brew.ModBrews;

@Mixin(value = ModBrews.class, remap = false)
public abstract class ModBrewsMixin {

    @ModifyConstant(method = "initTC", constant = @Constant(intValue = 12000))
    private static int increaseDuration(int value) {
        return 1728000;
    }
}
