package com.xinyihl.whimcraft.common.mixins.astralsorcery;

import com.xinyihl.whimcraft.Configurations;
import hellfirepvp.astralsorcery.common.constellation.perk.PerkLevelManager;
import net.minecraftforge.common.config.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PerkLevelManager.class, remap = false)
public abstract class PerkLevelManagerMixin {
    @Redirect(
            method = "loadFromConfig",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/config/Configuration;getInt(Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;)I"
            )
    )
    public int injected(Configuration instance, String a, String b, int c, int d, int e, String f) {
        return Configurations.ASMOD_CONFIG.asLevelCap;
    }
}
