package com.xinyihl.whimcraft.common.mixins.mmce;

import com.xinyihl.whimcraft.common.title.base.TitleMEAspectBusMMCE;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import kport.modularmagic.common.crafting.helper.AspectProviderCopy;
import kport.modularmagic.common.crafting.requirement.RequirementAspect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = RequirementAspect.class, remap = false)
public class RequirementAspectMixin {
    /**
     * @author xin_yi_hl
     * @reason
     */
    @Overwrite
    @Nonnull
    public List<ProcessingComponent<?>> copyComponents(List<ProcessingComponent<?>> components) {
        List<ProcessingComponent<?>> list = new ArrayList<>();

        for (ProcessingComponent<?> component : components) {
            if (component.providedComponent() instanceof TitleMEAspectBusMMCE.MYAspectProviderCopy) {
                list.add(new ProcessingComponent(component.component(), new TitleMEAspectBusMMCE.MYAspectProviderCopy(((TitleMEAspectBusMMCE.MYAspectProviderCopy) component.providedComponent()).getOriginal()), component.tag()));
            } else {
                list.add(new ProcessingComponent(component.component(), new AspectProviderCopy(((AspectProviderCopy) component.providedComponent()).getOriginal()), component.tag()));
            }
        }

        return list;
    }
}
