package com.xinyihl.whimcraft.common.mixins.immersiveengineering;

import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.Crafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Crafting.class, remap = false)
public abstract class ManualPagesCraftingMixin {

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/lib/manual/ManualPages$Crafting;recalculateCraftingRecipes()V"
            )
    )
    private void removeCraftingRecipes(ManualPages.Crafting instance) {
        // NO-OP
    }
}
