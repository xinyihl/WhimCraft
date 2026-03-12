package com.xinyihl.whimcraft.common.mixins.mekanism;

import mekanism.common.integration.OreDictManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Remove default Log=>Plank recipes
 * for performance and unification reasons
 */
@Mixin(value = OreDictManager.class, remap = false)
public abstract class OreDictManagerMixin {

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lmekanism/common/integration/OreDictManager;addLogRecipes()V"
            )
    )
    private static void removeLogRecipes() {
        // NO-OP
    }
}
