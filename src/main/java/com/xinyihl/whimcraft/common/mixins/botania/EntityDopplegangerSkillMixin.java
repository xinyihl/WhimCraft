package com.xinyihl.whimcraft.common.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.entity.EntityDoppleganger;

@Mixin(value = EntityDoppleganger.class, remap = false)
public abstract class EntityDopplegangerSkillMixin {

    @Redirect(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F", remap = false)
    )
    private float whimCraft$removeDamageCap(float cap, float amount) {
        return amount;
    }
}