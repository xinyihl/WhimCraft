package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.crafting.CraftingLink;
import appeng.crafting.MECraftingInventory;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.MachineSource;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.xinyihl.whimcraft.common.init.IB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CraftingCPUCluster.class, remap = false)
public abstract class CraftingCPUClusterOrderMixin {
    @Shadow
    private IAEItemStack finalOutput;
    @Unique
    protected boolean wc$ghostInjecting;
    @Shadow
    private MachineSource machineSrc;

    @Shadow
    public abstract IAEItemStack injectItems(IAEItemStack input, Actionable type, IActionSource src);

    @Inject(
            method = "executeCrafting",
            at = @At("HEAD")
    )
    protected void executeCraftingHead(CallbackInfo ci, @Share(value = "wcvoid") LocalRef<List<IAEItemStack>> voidSet) {
        voidSet.set(new ArrayList<>());
    }

    @Inject(
            method = "executeCrafting",
            at = @At("RETURN")
    )
    protected void executeCraftingReturn(CallbackInfo ci, @Share(value = "wcvoid") LocalRef<List<IAEItemStack>> voidSet) {
        for (IAEItemStack output : voidSet.get()) {
            this.wc$ghostInject(output);
        }
    }

    @Unique
    protected void wc$ghostInject(IAEItemStack output) {
        this.wc$ghostInjecting = true;
        try {
            this.injectItems(output, Actionable.MODULATE, this.machineSrc);
        } finally {
            this.wc$ghostInjecting = false;
        }
    }

    @ModifyExpressionValue(
            method = "executeCrafting",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/api/networking/crafting/ICraftingPatternDetails;getCondensedOutputs()[Lappeng/api/storage/data/IAEItemStack;"
            )
    )
    protected IAEItemStack[] modifyCondensedOutputs(IAEItemStack[] original, @Share(value = "wcvoid") LocalRef<List<IAEItemStack>> voidSet) {
        if (IB.itemOrder != null && this.finalOutput.getItem() == IB.itemOrder) {
            for (IAEItemStack output : original) {
                if (output.getItem() == IB.itemOrder) {
                    voidSet.get().add(output);
                }
            }
        }
        return original;
    }

    @WrapOperation(
            method = "injectItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/crafting/CraftingLink;injectItems(Lappeng/api/storage/data/IAEItemStack;Lappeng/api/config/Actionable;)Lappeng/api/storage/data/IAEItemStack;"
            )
    )
    protected IAEItemStack wrapInjectItems(CraftingLink link, IAEItemStack item, Actionable actionable, Operation<IAEItemStack> operation) {
        if (this.wc$ghostInjecting) {
            return null;
        }
        return operation.call(link, item, actionable);
    }

    @WrapOperation(
            method = "injectItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/crafting/MECraftingInventory;injectItems(Lappeng/api/storage/data/IAEItemStack;Lappeng/api/config/Actionable;Lappeng/api/networking/security/IActionSource;)Lappeng/api/storage/data/IAEItemStack;"
            )
    )
    protected IAEItemStack wrapInjectItems(MECraftingInventory link, IAEItemStack item, Actionable actionable, IActionSource source, Operation<IAEItemStack> operation) {
        if (this.wc$ghostInjecting) {
            return null;
        }
        return operation.call(link, item, actionable, source);
    }
}