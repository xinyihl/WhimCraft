package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.core.sync.packets.PacketJEIRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PacketJEIRecipe.class, remap = false)
public abstract class PacketJEIRecipeMixin {
    @Redirect(
            method = "serverPacketData",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/api/storage/data/IAEItemStack;setStackSize(J)Lappeng/api/storage/data/IAEStack;",
                    ordinal = 0
            )
    )
    public IAEStack injected(IAEItemStack instance, long l) {
        return instance;
    }
}
