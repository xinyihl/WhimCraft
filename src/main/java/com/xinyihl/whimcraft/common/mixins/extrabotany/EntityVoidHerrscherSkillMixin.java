package com.xinyihl.whimcraft.common.mixins.extrabotany;

import com.meteor.extrabotany.common.entity.gaia.EntityVoidHerrscher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityVoidHerrscher.class, remap = false)
public abstract class EntityVoidHerrscherSkillMixin {
    @Redirect(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F", remap = false)
    )
    private float whimCraft$removeDamageCap(float cap, float amount) {
        return amount;
    }

    @Redirect(
            method = "setHealth",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", remap = false)
    )
    public float whimCraft$removeSetHealthCap(float a, float b) {
        return a;
    }
}
