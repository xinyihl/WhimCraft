package com.xinyihl.whimcraft.common.mixins.thaumcraft;

import com.xinyihl.whimcraft.common.integration.crafttweaker.WhimCraftUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.common.world.aura.AuraHandler;

@Mixin(value = AuraHandler.class, remap = false)
public abstract class AuraHandlerMixin {
    @Shadow
    public static void addAuraChunk(int dim, Chunk chunk, short base, float vis, float flux) {
    }

    @Redirect(
            method = "generateAura",
            at = @At(value = "INVOKE", target = "Lthaumcraft/common/world/aura/AuraHandler;addAuraChunk(ILnet/minecraft/world/chunk/Chunk;SFF)V")
    )
    private static void injected(int dim, Chunk chunk, short base, float vis, float flux) {
        Biome bgb = chunk.getWorld().getBiome(new BlockPos(chunk.x * 16 + 8, 50, chunk.z * 16 + 8));
        int id = Biome.getIdForBiome(bgb);
        if (WhimCraftUtils.generateBiomeAuraBase.containsKey(id)) {
            short newBase = WhimCraftUtils.generateBiomeAuraBase.get(id);
            addAuraChunk(chunk.getWorld().provider.getDimension(), chunk, newBase, newBase, 0.0F);
            return;
        }
        addAuraChunk(chunk.getWorld().provider.getDimension(), chunk, base, base, 0.0F);
    }
}
