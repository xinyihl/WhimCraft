package com.xinyihl.whimcraft.common.mixins.enderio;

import crazypants.enderio.machines.integration.jei.TankRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Remove ~40000 pointless recipes from [Fluid Tank] such as "Any Tank + Any Fluid => Tank With Fluid".
 * Only actual tank recipes (item+fluid=>item) will be shown. This saves 2-6 seconds of modpack loading time.
 */
@Mixin(value = TankRecipeCategory.class, remap = false)
public abstract class TankRecipeCategoryMixin {

    @Redirect(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Boolean;booleanValue()Z",
                    ordinal = 0
            )
    )
    private static boolean registerTankRecipeCategory(Boolean instance) {
        return true;
    }
}
