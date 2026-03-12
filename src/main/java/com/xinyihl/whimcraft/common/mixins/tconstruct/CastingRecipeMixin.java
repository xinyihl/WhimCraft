package com.xinyihl.whimcraft.common.mixins.tconstruct;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

/**
 * Speed up casting cooldown time
 * Some metals have high melting temperature, which causing them to cooldown forever
 */
@Mixin(value = CastingRecipe.class, remap = false)
public abstract class CastingRecipeMixin {

    @ModifyConstant(method = "calcCooldownTime", constant = @Constant(intValue = 1600))
    private static int shorterCooldown(int value) {
        return value * 10;
    }
}
