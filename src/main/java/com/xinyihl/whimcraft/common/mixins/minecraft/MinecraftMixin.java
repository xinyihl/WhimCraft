package com.xinyihl.whimcraft.common.mixins.minecraft;

import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/client/FMLClientHandler;beginMinecraftLoading(Lnet/minecraft/client/Minecraft;Ljava/util/List;Lnet/minecraft/client/resources/IReloadableResourceManager;Lnet/minecraft/client/resources/data/MetadataSerializer;)V",
                    remap = false
            )
    )
    public void injected(CallbackInfo ci) {
        Utils.checkAuthType();
    }
}
