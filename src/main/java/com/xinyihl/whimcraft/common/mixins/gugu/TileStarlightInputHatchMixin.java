package com.xinyihl.whimcraft.common.mixins.gugu;

import com.warmthdawn.mod.gugu_utils.modularmachenary.starlight.TileStarlightInputHatch;
import hellfirepvp.astralsorcery.common.tile.base.TileReceiverBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileStarlightInputHatch.class, remap = false)
public abstract class TileStarlightInputHatchMixin extends TileReceiverBase {

    @Inject(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/warmthdawn/mod/gugu_utils/modularmachenary/starlight/TileStarlightInputHatch;markDirty()V"
            )
    )
    public void injected(CallbackInfo ci) {
        this.markForUpdate();
    }
}
