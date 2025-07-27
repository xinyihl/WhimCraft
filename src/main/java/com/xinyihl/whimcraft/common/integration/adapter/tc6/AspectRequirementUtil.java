package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMRequirements;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.init.Mods;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.crafting.requirement.types.ModularMagicRequirements;
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

    public static RequirementType<?, ?> getRequirementType() {
        switch (Configurations.ADAPTER_CONFIG.aspectType) {
            case "mmce": {
                if (Mods.MMCE.isLoaded()) return getRequirementTypeMMCE();
                throw new RuntimeException("Unload mod:  modularmachinery");
            }
            case "gugu": {
                if (Mods.GUGU.isLoaded()) return getRequirementTypeGUGU();
                throw new RuntimeException("Unload mod:  gugu-utils");
            }
            case "mmce-addons": {
                if (Mods.MMADDONS.isLoaded()) return getRequirementTypeMMCEADDONS();
                throw new RuntimeException("Unload mod:  modularmachineryaddons");
            }
        }
        throw new RuntimeException("Unknown AspectType: " + Configurations.ADAPTER_CONFIG.aspectType);
    }

    @Optional.Method(modid = "modularmachinery")
    private static RequirementType<?, ?> getRequirementTypeMMCE() {
        return RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMagicRequirements.KEY_REQUIREMENT_ASPECT);
    }


    @Optional.Method(modid = "modularmachinery")
    private static ComponentRequirement<?, ?> getRequirementMMCE(IOType actionType, int amount, Aspect aspect) {
        return new kport.modularmagic.common.crafting.requirement.RequirementAspect(actionType, amount, aspect);
    }

    @Optional.Method(modid = "gugu-utils")
    private static RequirementType<?, ?> getRequirementTypeGUGU() {
        return (com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.types.RequirementTypeAspect) MMRequirements.REQUIREMENT_TYPE_ASPECT;
    }

    @Optional.Method(modid = "gugu-utils")
    private static ComponentRequirement<?, ?> getRequirementGUGU(IOType actionType, int amount, Aspect aspect) {
        if (actionType == IOType.INPUT) {
            return com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect.createInput(amount, aspect);
        }
        if (actionType == IOType.OUTPUT) {
            return new com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspectOutput(amount, aspect);
        }
        throw new RuntimeException("Unknown IOType: " + actionType);
    }

    @Optional.Method(modid = "modularmachineryaddons")
    private static RequirementType<?, ?> getRequirementTypeMMCEADDONS() {
        return RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_ESSENTIA);
    }

    @Optional.Method(modid = "modularmachineryaddons")
    private static ComponentRequirement<?, ?> getRequirementMMCEADDONS(IOType actionType, int amount, Aspect aspect) {
        return github.alecsio.mmceaddons.common.crafting.requirement.thaumicenergistics.RequirementEssentia.from(actionType, aspect.getTag(), amount);
    }
}
