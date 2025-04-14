package com.xinyihl.whimcraft.common.mixins.mmce;

import com.xinyihl.whimcraft.common.integration.adapter.RegRecipeAdapters;
import hellfirepvp.modularmachinery.common.registry.RegistryRecipeAdapters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RegistryRecipeAdapters.class, remap = false)
public abstract class RegistryRecipeAdaptersMixin {
    @Inject(
            method = "initialize",
            at = @At(
                    value = "HEAD"
            )
    )
    private static void injected(CallbackInfo ci) {
        RegRecipeAdapters.initialize();
    }
}
