package com.xinyihl.whimcraft.common.mixins.astralsorcery;

import hellfirepvp.astralsorcery.common.crafting.IGatedRecipe;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileAltar.class, remap = false)
public abstract class TileAltarMixin {
    @Redirect(
            method = "findRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "hellfirepvp/astralsorcery/common/crafting/IGatedRecipe.hasProgressionServer(Lnet/minecraft/entity/player/EntityPlayer;)Z"
            )
    )
    private boolean injected(IGatedRecipe instance, EntityPlayer player) {
        return true;
    }
}
