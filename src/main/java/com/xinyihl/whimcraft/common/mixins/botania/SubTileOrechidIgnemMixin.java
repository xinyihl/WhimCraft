package com.xinyihl.whimcraft.common.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.subtile.functional.SubTileOrechidIgnem;

@Mixin(value = SubTileOrechidIgnem.class, remap = false)
public abstract class SubTileOrechidIgnemMixin {

    @Inject(method = "canOperate", at = @At("HEAD"), cancellable = true)
    private void alwaysCanOperate(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
