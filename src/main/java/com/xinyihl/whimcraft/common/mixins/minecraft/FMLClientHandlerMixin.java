package com.xinyihl.whimcraft.common.mixins.minecraft;

import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(value = FMLClientHandler.class, remap = false)
public abstract class FMLClientHandlerMixin {
    /**
     * @author MyServerIsCompatible
     * @reason Disable Forge's enhanced server list
     */
    @Nullable
    @Overwrite
    public String enhanceServerListEntry(ServerListEntryNormal serverListEntry, ServerData serverEntry, int x, int width, int y, int relativeMouseX, int relativeMouseY) {
        return null;
    }

}
