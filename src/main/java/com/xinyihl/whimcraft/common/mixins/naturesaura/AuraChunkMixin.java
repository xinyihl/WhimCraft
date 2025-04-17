package com.xinyihl.whimcraft.common.mixins.naturesaura;

import com.xinyihl.whimcraft.Configurations;
import de.ellpeck.naturesaura.chunk.AuraChunk;
import de.ellpeck.naturesaura.packet.PacketHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AuraChunk.class, remap = false)
public abstract class AuraChunkMixin {
    @Shadow
    private boolean needsSync;
    @Shadow
    @Final
    private Chunk chunk;
    @Shadow
    public abstract IMessage makePacket();
    @Inject(
            method = "update",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injected(CallbackInfo ci){
        if (!Configurations.NATURE_CONFIG.auraChunkUpdateEnable) {
            if (this.needsSync) {
                World world = this.chunk.getWorld();
                PacketHandler.sendToAllLoaded(world, new BlockPos(this.chunk.x * 16, 0, this.chunk.z * 16), this.makePacket());
                this.needsSync = false;
            }
            ci.cancel();
        }
    }
}
