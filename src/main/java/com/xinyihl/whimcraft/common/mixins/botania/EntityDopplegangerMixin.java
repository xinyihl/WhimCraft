package com.xinyihl.whimcraft.common.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.entity.EntityDoppleganger;

@Mixin(value = EntityDoppleganger.class, remap = false)
public abstract class EntityDopplegangerMixin {

    /**
     * Fix crash on fighting Gaia II on server when no player nearby
     * https://github.com/Krutoy242/Enigmatica2Expert-Extended/issues/344
     */
    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/botania/common/entity/EntityDoppleganger;setDead()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true,
            remap = true
    )
    private void stopUpdatingEntityWhenNoPlayerNearby(CallbackInfo ci) {
        ci.cancel();
    }
}
