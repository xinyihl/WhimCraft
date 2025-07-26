package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.init.Mods;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.machine.IOType;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.aspects.Aspect;

public class AspectRequirementUtil {
    public static ComponentRequirement<?, ?> getRequirement(IOType actionType, int amount, Aspect aspect) {
        switch (Configurations.ADAPTER_CONFIG.aspectType) {
            case "mmce": {
                if (Mods.MMCE.isLoaded()) return getRequirementMMCE(actionType, amount, aspect);
                throw new RuntimeException("Unload mod:  modularmachinery");
            }
            case "gugu": {
                if (Mods.GUGU.isLoaded()) return getRequirementGUGU(actionType, amount, aspect);
                throw new RuntimeException("Unload mod:  gugu-utils");
            }
            case "mmce-addons": {
                if (Mods.MMADDONS.isLoaded()) return getRequirementMMCEADDONS(actionType, amount, aspect);
                throw new RuntimeException("Unload mod:  modularmachineryaddons");
            }
        }
        throw new RuntimeException("Unknown AspectType: " + Configurations.ADAPTER_CONFIG.aspectType);
    }

    @Optional.Method(modid = "modularmachinery")
    private static ComponentRequirement<?, ?> getRequirementMMCE(IOType actionType, int amount, Aspect aspect) {
        return new kport.modularmagic.common.crafting.requirement.RequirementAspect(actionType, amount, aspect);
    }

    @Optional.Method(modid = "gugu-utils")
    private static ComponentRequirement<?, ?> getRequirementGUGU(IOType actionType, int amount, Aspect aspect) {
        switch (actionType) {
            case INPUT:
                return com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect.createInput(amount, aspect);
            case OUTPUT:
                return new com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspectOutput(amount, aspect);
        }
        return null;
    }

    @Optional.Method(modid = "modularmachineryaddons")
    private static ComponentRequirement<?, ?> getRequirementMMCEADDONS(IOType actionType, int amount, Aspect aspect) {
        return github.alecsio.mmceaddons.common.crafting.requirement.thaumicenergistics.RequirementEssentia.from(actionType, aspect.getTag(), amount);
    }
}
