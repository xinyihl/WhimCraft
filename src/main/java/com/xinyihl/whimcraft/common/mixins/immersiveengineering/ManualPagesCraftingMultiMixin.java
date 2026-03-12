package com.xinyihl.whimcraft.common.mixins.immersiveengineering;

import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.CraftingMulti;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CraftingMulti.class, remap = false)
public abstract class ManualPagesCraftingMultiMixin {

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lblusunrize/lib/manual/ManualPages$CraftingMulti;recalculateCraftingRecipes()V"
            )
    )
    private void removeCraftingRecipes(ManualPages.CraftingMulti instance) {
        // NO-OP
    }
}
