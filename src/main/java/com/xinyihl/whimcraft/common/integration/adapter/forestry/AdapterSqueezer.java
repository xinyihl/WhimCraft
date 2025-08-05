package com.xinyihl.whimcraft.common.integration.adapter.forestry;

import crafttweaker.util.IEventHandler;
import forestry.api.recipes.RecipeManagers;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementFluid;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.*;

public class AdapterSqueezer extends RecipeAdapter {
    public AdapterSqueezer() {
        super(new ResourceLocation("forestry", "whimcraft_squeezer"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();
        RecipeManagers.squeezerManager.recipes().forEach(recipe -> {
            int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, recipe.getProcessingTime(), false));
            if (inDuration <= 0) {
                return;
            }
            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("forestry", "whimcraft_auto_squeezer" + incId),
                    owningMachineName,
                    inDuration,
                    incId, false);
            recipe.getResources().forEach(resource -> {
                int inAmount1 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, resource.getCount(), false));
                if (inAmount1 > 0) {
                    if(!resource.isEmpty()) {
                        machineRecipe.addRequirement(new RequirementItem(IOType.INPUT, ItemUtils.copyStackWithSize(resource, inAmount1)));
                    }
                }
            });
            int inAmount2 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_FLUID, IOType.OUTPUT, recipe.getFluidOutput().amount, false));
            if (inAmount2 > 0) {
                FluidStack outputFluid = recipe.getFluidOutput().copy();
                outputFluid.amount = inAmount2;
                machineRecipe.addRequirement(new RequirementFluid(IOType.OUTPUT, outputFluid));
            }
            int inAmount3 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, recipe.getRemnants().getCount(), false));
            if (inAmount3 > 0) {
                if (!recipe.getRemnants().isEmpty()) {
                    RequirementItem requirementItem = new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(recipe.getRemnants(), inAmount3));
                    requirementItem.setChance(recipe.getRemnantsChance());
                    machineRecipe.addRequirement(requirementItem);
                }
            }
            machineRecipeList.add(machineRecipe);
            incId++;
        });
        return machineRecipeList;
    }
}
