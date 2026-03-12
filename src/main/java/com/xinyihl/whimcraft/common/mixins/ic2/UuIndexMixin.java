package com.xinyihl.whimcraft.common.mixins.ic2;

import ic2.core.uu.UuIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Remove UU-values calculating on game load
 * Fix strange bug, when predefined values was rewritten with wrong ones.
 * Also, this would remove some small UU recipes, like wooden slabs or buttons.
 */
@Mixin(value = UuIndex.class, remap = false)
public abstract class UuIndexMixin {

    /**
     * @author WhimCraft
     * @reason Disable UU index init for performance and correctness
     */
    @Overwrite
    public void init() {
        // NO-OP
    }
}
