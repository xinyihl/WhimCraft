package com.xinyihl.whimcraft.common.mixins.extrautils2;

import com.rwtema.extrautils2.tile.TileScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Make Screen not consume power
 */
@Mixin(value = TileScreen.class, remap = false)
public abstract class TileScreenMixin {

    @ModifyConstant(method = "getPower", constant = @Constant(floatValue = 1.0f))
    private float setPowerToZero(float value) {
        return 0.0f;
    }
}
