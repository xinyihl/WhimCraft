package com.xinyihl.whimcraft.common.mixins.enderio;

import crazypants.enderio.base.recipe.alloysmelter.AlloyRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Increase [Alloy Smelter] performance
 * <p>
 * Registering recipes required iterate over big amounts of registry to
 * find what custom recipes could be x3, like when you can smelt 3 cobblestone at once.
 * This takes 1-3 seconds on loading time for about ~16 custom recipes.
 */
@Mixin(value = AlloyRecipeManager.class, remap = false)
public abstract class AlloyRecipeManagerMixin {

    @Redirect(
            method = "addRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lcrazypants/enderio/base/recipe/alloysmelter/AlloyRecipeManager;needsSynthetics(Lcrazypants/enderio/base/recipe/Recipe;)Z"
            )
    )
    private boolean noSyntheticRecipe(Object instance, Object recipe) {
        return false;
    }
}
