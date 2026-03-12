package com.xinyihl.whimcraft.common.mixins.thermalfoundation;

import cofh.thermalfoundation.init.TFEquipment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TFEquipment.class, remap = false)
public abstract class TFEquipmentMixin {

    /**
     * @author WhimCraft
     * @reason Remove thermal tools
     */
    @Overwrite
    public static void preInit() {
        // NO-OP: remove thermal tools
    }
}
