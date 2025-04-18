package com.xinyihl.whimcraft.common.mixins.extrautils2;

import com.xinyihl.whimcraft.Configurations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(targets = "com.rwtema.extrautils2.tile.TileRainbowGenerator$1", remap = false)
public abstract class TileRainbowGenerator$1Mixin {
    @ModifyConstant(
            method = {
                    "getEnergyStored",
                    "getMaxEnergyStored"
            },
            constant = @Constant(intValue = 25000000)

    )
    private int injected(int constant) {
        return Configurations.EXTRA_CONFIG.rainbowGeneratorEnergy;
    }
}
