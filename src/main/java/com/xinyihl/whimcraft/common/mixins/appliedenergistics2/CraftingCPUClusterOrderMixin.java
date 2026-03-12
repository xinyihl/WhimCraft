package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import com.xinyihl.whimcraft.common.init.IB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftingCPUCluster.class, remap = false)
public abstract class CraftingCPUClusterOrderMixin {

    @Shadow
    private IItemList<IAEItemStack> waitingFor;

    @Shadow
    private IAEItemStack finalOutput;

    @Shadow
    private boolean isComplete;

    @Shadow
    private void completeJob() {
    }

    @Inject(method = "injectItems", at = @At("RETURN"))
    private void autoCompleteWhenDispatched(IAEItemStack input, Actionable type, IActionSource src, CallbackInfoReturnable<IAEItemStack> cir) {
        if (IB.itemOrder == null || this.isComplete || this.finalOutput == null) {
            return;
        }
        if (this.finalOutput.getItem() != IB.itemOrder) {
            return;
        }
        if (this.waitingFor != null && this.waitingFor.isEmpty()) {
            this.completeJob();
        }
    }
}