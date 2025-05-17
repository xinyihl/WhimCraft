package com.xinyihl.whimcraft.common.mixins.tcomplement;

import knightminer.tcomplement.melter.tileentity.TileMelter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileMelter.class, remap = false)
public class TileMelterMixin {

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void injected(CallbackInfo ci) {
        ((TileMelter) (Object) this).resize(3);
    }
}
