package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.xinyihl.whimcraft.Configurations;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.machine.IOType;
import thaumcraft.api.aspects.Aspect;

public class AspectRequirementUtil {
    public static ComponentRequirement<?, ?> getRequirement(IOType actionType, int amount, Aspect aspect) {
        switch (Configurations.ADAPTER_CONFIG.aspectType) {
            case "mmce": {
                return new kport.modularmagic.common.crafting.requirement.RequirementAspect(actionType, amount, aspect);
            }
            case "gugu": {
                switch (actionType) {
                    case INPUT:
                        return com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect.createInput(amount, aspect);
                    case OUTPUT:
                        return new com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspectOutput(amount, aspect);
                }
            }
            case "mmce-addons": {
                return github.alecsio.mmceaddons.common.crafting.requirement.thaumicenergistics.RequirementEssentia.from(actionType, aspect.getTag(), amount);
            }
        }
        throw new RuntimeException("Unknown aspect type: " + Configurations.ADAPTER_CONFIG.aspectType);
    }
}
