package com.xinyihl.whimcraft.common.integration.adapter.ie;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import crafttweaker.util.IEventHandler;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import github.kasuminova.mmce.common.itemtype.ChancedIngredientStack;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementEnergy;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementIngredientArray;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe.recipeList;

public class AdapterIEArcFurnace extends RecipeAdapter {
    public AdapterIEArcFurnace() {
        super(new ResourceLocation("immersiveengineering", "whimcraft_arcfurnace"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();

        recipeList.forEach((recipe) -> {
            if (recipe.input == null) {
                return;
            }
            if (recipe.additives == null) {
                return;
            }
            if (recipe.output == null) {
                return;
            }
            int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, recipe.getTotalProcessTime(), false));
            if (inDuration <= 0) {
                return;
            }
            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("immersiveengineering", "whimcraft_auto_arcfurnace" + incId),
                    owningMachineName,
                    inDuration,
                    incId, false);

            // Item Input
            int inAmount1 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, recipe.input.inputSize, false));
            if (inAmount1 <= 0) {
                return;
            }
            List<ChancedIngredientStack> inputMainList1 = recipe.input.getStackList().stream()
                    .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount1)))
                    .collect(Collectors.toList());

            if (inputMainList1.isEmpty()) return;
            machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList1));

            for (IngredientStack ingredientStack : recipe.additives) {
                int inAmount2 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, ingredientStack.inputSize, false));
                if (inAmount2 <= 0) {
                    continue;
                }
                List<ChancedIngredientStack> inputMainList2 = ingredientStack.getStackList().stream()
                        .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount2)))
                        .collect(Collectors.toList());

                if (!inputMainList2.isEmpty()) {
                    machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList2));
                }
            }

            // Energy
            int energyPerTick = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ENERGY, IOType.INPUT, (float) recipe.getTotalProcessEnergy() / (float) recipe.getTotalProcessTime(), false));
            if (energyPerTick > 0) {
                machineRecipe.addRequirement(new RequirementEnergy(IOType.INPUT, energyPerTick));
            }

            // Output
            ItemStack output = recipe.output;
            if (output.isEmpty()) return;
            int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
            if (outAmount > 0) {
                int[] ids = OreDictionary.getOreIDs(output);
                if (ids.length > 0) {
                    machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, OreDictionary.getOreName(ids[0]), outAmount));
                } else {
                    machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
                }
            }

            machineRecipeList.add(machineRecipe);
            incId++;
        });
        return machineRecipeList;
    }
}
