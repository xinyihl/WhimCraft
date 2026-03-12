package com.xinyihl.whimcraft.common.mixins.forestry;

import forestry.api.recipes.IFermenterRecipe;
import forestry.factory.recipes.FermenterRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Remove default no-fermentated recipe flood
 * for better performance and less HEI junk.
 */
@Mixin(value = FermenterRecipeManager.class, remap = false)
public abstract class FermenterRecipeManagerMixin {

    @Inject(
            method = "addRecipe(Lforestry/api/recipes/IFermenterRecipe;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void removeRecipe(IFermenterRecipe recipe, CallbackInfoReturnable<Boolean> cir) {
        if (recipe.getFermentationValue() <= 0) {
            cir.setReturnValue(false);
        }
    }
}
