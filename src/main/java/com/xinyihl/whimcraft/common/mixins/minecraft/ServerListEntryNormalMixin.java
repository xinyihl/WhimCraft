package com.xinyihl.whimcraft.common.mixins.minecraft;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerListEntryNormal.class)
public abstract class ServerListEntryNormalMixin {

    @Shadow
    @Final
    private ServerData server;

    @Redirect(
            method = "drawEntry",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/multiplayer/ServerData;version:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    public int injected(ServerData version) {
        return 340;
    }

    @Redirect(
            method = "drawEntry",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I",
                    ordinal = 2
            )
    )
    public int injecteda(FontRenderer fontRenderer, String text, int x, int y, int color) {
        int i = fontRenderer.getStringWidth(text);
        int j = fontRenderer.getStringWidth(server.gameVersion);
        if ("1.12.2".equals(server.gameVersion)) return 0;
        return fontRenderer.drawString(server.gameVersion, x + i - j, y, color);
    }
}
