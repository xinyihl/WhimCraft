package com.xinyihl.whimcraft.common.integration.adapter.forestry;

import crafttweaker.util.IEventHandler;
import forestry.api.recipes.RecipeManagers;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AdapterCentrifuge extends RecipeAdapter {
    public AdapterCentrifuge() {
        super(new ResourceLocation("forestry", "whimcraft_centrifuge"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();
        RecipeManagers.centrifugeManager.recipes().forEach(recipe -> {
            if(recipe.getInput().isEmpty()) {
                return;
            }
            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("forestry", "whimcraft_auto_centrifuge" + incId),
                    owningMachineName,
                    recipe.getProcessingTime(),
                    incId, false);
            ItemStack input = recipe.getInput();
            int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, input.getCount(), false));
            if (inAmount > 0) {
                machineRecipe.addRequirement(new RequirementItem(IOType.INPUT, ItemUtils.copyStackWithSize(input, inAmount)));
            }
            recipe.getAllProducts().forEach((itemStack, aFloat) -> {
                int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, itemStack.getCount(), false));
                if (outAmount > 0) {
                    if (!itemStack.isEmpty()) {
                        RequirementItem requirementItem = new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(itemStack, outAmount));
                        requirementItem.setChance(aFloat);
                        machineRecipe.addRequirement(requirementItem);
                    }
                }
            });
            machineRecipeList.add(machineRecipe);
            incId++;
        });
        return machineRecipeList;
    }
}
