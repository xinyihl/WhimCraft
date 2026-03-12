package com.xinyihl.whimcraft.common.mixins.smelteryio;

import mctmods.smelteryio.blocks.meta.EnumMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Fixed Smeltery IO blocks' harvestLevel & resistance
 */
@Mixin(value = EnumMachine.class, remap = false)
public abstract class EnumMachineMixin {

    /**
     * @author WhimCraft
     * @reason Fix harvest level to -1
     */
    @Overwrite
    public int getHarvestLevel() {
        return -1;
    }

    /**
     * @author WhimCraft
     * @reason Set proper resistance value
     */
    @Overwrite
    public float getResistance() {
        return 20.0f;
    }
}
