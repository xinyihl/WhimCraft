package com.xinyihl.whimcraft.common.mixins.ae2fc;

import appeng.items.misc.ItemEncodedPattern;
import com.glodblock.github.common.item.ItemFluidEncodedPattern;
import com.glodblock.github.common.item.ItemLargeEncodedPattern;
import com.xinyihl.whimcraft.Configurations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ItemLargeEncodedPattern.class, ItemFluidEncodedPattern.class}, remap = false)
public abstract class ItemEncodedPatternMixin extends ItemEncodedPattern {
    @Inject(
            method = "addCheckedInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/items/misc/ItemEncodedPattern;addCheckedInformation(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void injected(CallbackInfo ci) {
        if (Configurations.AEMOD_CONFIG.patternEncoder) {
            ci.cancel();
        }
    }
}
