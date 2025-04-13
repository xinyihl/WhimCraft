package com.xinyihl.whimcraft.common.mixins.gugu;

import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspectOutput;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.basic.RequirementConsumeOnce;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.types.RequirementTypeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentOutputRestrictor;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.machine.IOType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = RequirementAspectOutput.class, remap = false)
public abstract class RequirementAspectOutputMixin extends RequirementConsumeOnce<Integer, RequirementAspect.RT> {
    public RequirementAspectOutputMixin(RequirementTypeAdapter<Integer> componentType, IOType actionType) {
        super(componentType, actionType);
    }

    @Inject(
            method = "canStartCrafting",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void injected(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> list, CallbackInfoReturnable<CraftCheck> cir) {
        cir.setReturnValue(super.canStartCrafting(component, context, list));
    }
}
