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
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.xinyihl.whimcraft.Configurations.ADAPTER_CONFIG;

public class AdapterTC6Arcane extends RecipeAdapter {

    private final boolean isShaped;

    public AdapterTC6Arcane(boolean isShaped) {
        super(new ResourceLocation("thaumcraft", "whimcraft_arcane" + (isShaped ? "_shaped" : "_shapeless")));
        this.isShaped = isShaped;
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();

        for (ResourceLocation recipeName : CraftingManager.REGISTRY.getKeys()) {
            IRecipe tcRecipe = CraftingManager.REGISTRY.getObject(recipeName);
            if (tcRecipe instanceof IArcaneRecipe) {
                if (isShaped) {
                    if (!(tcRecipe instanceof ShapedArcaneRecipe)) {
                        continue;
                    }
                    ShapedArcaneRecipe recipe = (ShapedArcaneRecipe) tcRecipe;
                    if (recipe.getIngredients().isEmpty()) {
                        continue;
                    }
                    if (recipe.getRecipeOutput().isEmpty()) {
                        continue;
                    }
                    int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
                    if (inAmount <= 0) {
                        continue;
                    }

                    int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, ADAPTER_CONFIG.arcaneTime, false));
                    if (inDuration <= 0) {
                        continue;
                    }
                    MachineRecipe machineRecipe = createRecipeShell(
                            new ResourceLocation("thaumcraft", "whimcraft_auto_arcane_shaped" + incId),
                            owningMachineName,
                            inDuration,
                            incId, false);

                    // Item Input
                    recipe.getIngredients().forEach(ingredient -> {
                        ItemStack[] inputMain = ingredient.getMatchingStacks();
                        List<ChancedIngredientStack> inputMainList = Arrays.stream(inputMain)
                                .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount)))
                                .collect(Collectors.toList());
                        if (!inputMainList.isEmpty()) {
                            machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList));
                        }
                    });

                    // Aspect Inputs
                    if (recipe.getCrystals() != null) {
                        recipe.getCrystals().aspects.forEach((aspect, amount) -> {
                            int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, AspectRequirementUtil.getRequirementType(), IOType.INPUT, amount, false));
                            if (inAmounta <= 0) {
                                return;
                            }
                            machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.INPUT, inAmounta, aspect));
                        });
                    }

                    // Output
                    ItemStack output = recipe.getRecipeOutput();
                    int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
                    if (outAmount > 0) {
                        machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
                    }

                    machineRecipeList.add(machineRecipe);
                    incId++;
                } else {
                    if (!(tcRecipe instanceof ShapelessArcaneRecipe)) {
                        continue;
                    }
                    ShapelessArcaneRecipe recipe = (ShapelessArcaneRecipe) tcRecipe;
                    if (recipe.getIngredients().isEmpty()) {
                        continue;
                    }
                    if (recipe.getRecipeOutput().isEmpty()) {
                        continue;
                    }
                    int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
                    if (inAmount <= 0) {
                        continue;
                    }

                    int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, ADAPTER_CONFIG.arcaneTime, false));
                    if (inDuration <= 0) {
                        continue;
                    }
                    MachineRecipe machineRecipe = createRecipeShell(
                            new ResourceLocation("thaumcraft", "whimcraft_auto_arcane_shapeless" + incId),
                            owningMachineName,
                            inDuration,
                            incId, false);

                    // Item Input
                    recipe.getIngredients().forEach(ingredient -> {
                        ItemStack[] inputMain = ingredient.getMatchingStacks();
                        List<ChancedIngredientStack> inputMainList = Arrays.stream(inputMain)
                                .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount)))
                                .collect(Collectors.toList());
                        if (!inputMainList.isEmpty()) {
                            machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList));
                        }
                    });

                    // Aspect Inputs
                    if (recipe.getCrystals() != null) {
                        recipe.getCrystals().aspects.forEach((aspect, amount) -> {
                            int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, AspectRequirementUtil.getRequirementType(), IOType.INPUT, amount, false));
                            if (inAmounta <= 0) {
                                return;
                            }
                            machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.INPUT, inAmounta, aspect));
                        });
                    }

                    // Output
                    ItemStack output = recipe.getRecipeOutput();
                    int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
                    if (outAmount > 0) {
                        machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
                    }

                    machineRecipeList.add(machineRecipe);
                    incId++;
                }
            }
        }

        ThaumcraftApi.getCraftingRecipes().forEach((recipeName, tcRecipe) -> {

        });

        return machineRecipeList;
    }
}
