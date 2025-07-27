package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.warmthdawn.mod.gugu_utils.modularmachenary.MMRequirements;
import com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.types.RequirementTypeAspect;
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
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
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

        ThaumcraftApi.getCraftingRecipes().forEach((recipeName, tcRecipe) -> {
            if (isShaped) {
                if (!(tcRecipe instanceof ShapedArcaneRecipe)) {
                    return;
                }
                ShapedArcaneRecipe recipe = (ShapedArcaneRecipe) tcRecipe;
                if (recipe.getIngredients().isEmpty()) {
                    return;
                }
                if (recipe.getRecipeOutput().isEmpty()) {
                    return;
                }
                int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
                if (inAmount <= 0) {
                    return;
                }

                int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, ADAPTER_CONFIG.arcaneTime, false));
                if (inDuration <= 0) {
                    return;
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
                recipe.getCrystals().aspects.forEach((aspect, amount) -> {
                    int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, (RequirementTypeAspect) MMRequirements.REQUIREMENT_TYPE_ASPECT, IOType.INPUT, amount, false));
                    if (inAmounta <= 0) {
                        return;
                    }
                    machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.INPUT, inAmounta, aspect));
                });

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
                    return;
                }
                ShapelessArcaneRecipe recipe = (ShapelessArcaneRecipe) tcRecipe;
                if (recipe.getIngredients().isEmpty()) {
                    return;
                }
                if (recipe.getRecipeOutput().isEmpty()) {
                    return;
                }
                int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
                if (inAmount <= 0) {
                    return;
                }

                int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, ADAPTER_CONFIG.arcaneTime, false));
                if (inDuration <= 0) {
                    return;
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
                recipe.getCrystals().aspects.forEach((aspect, amount) -> {
                    int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, (RequirementTypeAspect) MMRequirements.REQUIREMENT_TYPE_ASPECT, IOType.INPUT, amount, false));
                    if (inAmounta <= 0) {
                        return;
                    }
                    machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.INPUT, inAmounta, aspect));
                });

                // Output
                ItemStack output = recipe.getRecipeOutput();
                int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
                if (outAmount > 0) {
                    machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
                }

                machineRecipeList.add(machineRecipe);
                incId++;
            }
        });

        return machineRecipeList;
    }
}
