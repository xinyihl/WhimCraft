package com.xinyihl.whimcraft.common.mixins.neenergistics;

import com.github.vfyjxf.nee.jei.NEEJeiPlugin;
import mezz.jei.recipes.RecipeTransferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NEEJeiPlugin.class, remap = false)
public abstract class NEEJeiPluginMixin {
    @Inject(
            method = "replaceTransferHandler",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injected(RecipeTransferRegistry registry, CallbackInfo ci) {
        ci.cancel();
    }
}
