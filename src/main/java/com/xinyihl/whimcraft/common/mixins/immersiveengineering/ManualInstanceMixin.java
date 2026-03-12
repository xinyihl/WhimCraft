package com.xinyihl.whimcraft.common.mixins.immersiveengineering;

import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Remove crafting recipes in [Engineer's Manual]
 * <p>
 * This should save 2-6 seconds of game load.
 * Usually, it wont take so long for IE to make those 3x3 grid recipe pages.
 * But in modpacks there is a lot of recipes. IE needs to iterate all crafting table recipes for each manual book recipe.
 * Those recipes makes sense if modpack doesnt have HEI.
 * But since most of the recipes are changed and was already not actual, we remove them.
 * Text on pages still there, just without 3x3 grid.
 */
@Mixin(value = ManualInstance.class, remap = false)
public abstract class ManualInstanceMixin {

    @Redirect(
            method = "indexRecipes",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/lib/manual/IManualPage;recalculateCraftingRecipes()V"
            )
    )
    private void removeCraftingRecipes(IManualPage instance) {
        // NO-OP
    }
}
