package com.xinyihl.whimcraft.common.mixins.astralsorcery;

import hellfirepvp.astralsorcery.common.block.BlockCelestialCrystals;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockCelestialCrystals.class, remap = false)
public abstract class BlockCelestialCrystalsMixin {
    /**
     * @author xinyihl
     * @reason 允许非玩家挖掘水晶石矿石
     */
    @Overwrite
    private boolean checkSafety(World world, BlockPos pos) {
        return true;
    }
}
