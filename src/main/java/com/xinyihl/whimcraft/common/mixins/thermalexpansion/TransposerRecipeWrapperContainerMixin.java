package com.xinyihl.whimcraft.common.mixins.thermalexpansion;

import cofh.thermalexpansion.plugins.jei.machine.transposer.TransposerRecipeWrapperContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Map;

@Mixin(value = TransposerRecipeWrapperContainer.class, remap = false)
public abstract class TransposerRecipeWrapperContainerMixin {

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fluids/FluidRegistry;getRegisteredFluids()Ljava/util/Map;"
            )
    )
    private static Map<String, ?> removeFluidBucketRecipeEntries() {
        return Collections.emptyMap();
    }
}
