package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import crafttweaker.util.IEventHandler;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import github.kasuminova.mmce.common.itemtype.ChancedIngredientStack;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementIngredientArray;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionRecipe;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class AdapterTC6InfusionMatrix extends RecipeAdapter {
    public static final int BASE_WORK_TIME = 300;

    public AdapterTC6InfusionMatrix() {
        super(new ResourceLocation("thaumcraft", "whimcraft_infusion_matrix"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();

        ThaumcraftApi.getCraftingRecipes().forEach((recipeName, tcRecipe) -> {
            if (!(tcRecipe instanceof InfusionRecipe)) {
                return;
            }
            InfusionRecipe recipe = (InfusionRecipe) tcRecipe;
            if (recipe.getRecipeInput() == null) {
                return;
            }
            if (recipe.recipeOutput == null) {
                return;
            }
            int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
            if (inAmount <= 0) {
                return;
            }

            int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, recipe.instability == 0 ? BASE_WORK_TIME : recipe.instability * 1000, false));
            if (inDuration <= 0) {
                return;
            }

            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("thaumcraft", "whimcraft_auto_infusion" + incId),
                    owningMachineName,
                    inDuration,
                    incId, false);

            // Item Input
            ItemStack[] inputMain = recipe.getRecipeInput().getMatchingStacks();
            List<ChancedIngredientStack> inputMainList = Arrays.stream(inputMain)
                    .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount)))
                    .collect(Collectors.toList());
            if (!inputMainList.isEmpty()) {
                machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList));
            }

            // Input Components
            recipe.getComponents().stream()
                    .map(ingredient -> Arrays.stream(ingredient.getMatchingStacks())
                            .map(ChancedIngredientStack::new)
                            .collect(Collectors.toList()))
                    .filter(stackList -> !stackList.isEmpty())
                    .map(RequirementIngredientArray::new)
                    .forEach(machineRecipe::addRequirement);

            // Aspect Inputs
            recipe.getAspects().aspects.forEach((aspect, amount) -> {
                int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, AspectRequirementUtil.getRequirementType(), IOType.INPUT, amount, false));
                if (inAmounta <= 0) {
                    return;
                }
                machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.INPUT, inAmounta, aspect));
            });

            // Outputs
            boolean type = true;
            Object output = recipe.recipeOutput;
            if (output != null) {
                if (output instanceof ItemStack) {
                    int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, ((ItemStack) output).getCount(), false));
                    if (outAmount > 0) {
                        machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize((ItemStack) output, outAmount)));
                        type = false;
                    }
                } else {
                    Object[] objects = (Object[]) output;
                    for (ItemStack stack : recipe.getRecipeInput().getMatchingStacks()) {
                        if (stack == null || stack.isEmpty()) {
                            continue;
                        }
                        ItemStack copied = stack.copy();
                        copied.setTagInfo((String) objects[0], (NBTBase) objects[1]);
                        machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, copied));
                        type = false;
                    }
                }
            }
            if (type) return;
            machineRecipeList.add(machineRecipe);
            incId++;
        });

        return machineRecipeList;
    }
}
