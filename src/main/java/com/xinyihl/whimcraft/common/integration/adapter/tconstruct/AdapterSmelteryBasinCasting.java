package com.xinyihl.whimcraft.common.integration.adapter.tconstruct;

import crafttweaker.util.IEventHandler;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import github.kasuminova.mmce.common.itemtype.ChancedIngredientStack;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementFluid;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementIngredientArray;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdapterSmelteryBasinCasting extends RecipeAdapter {

    public AdapterSmelteryBasinCasting() {
        super(new ResourceLocation("mctsmelteryio", "whimcraft_smeltery_basin_casting"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();

        TinkerRegistry.getAllBasinCastingRecipes().forEach((irecipe -> {
            if (!(irecipe instanceof CastingRecipe)) return;
            CastingRecipe recipe = (CastingRecipe) irecipe;
            if (recipe.getFluid() == null) {
                return;
            }
            if (recipe.getResult() == null) {
                return;
            }

            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("mctsmelteryio", "whimcraft_auto_smeltery_basin_casting" + incId),
                    owningMachineName,
                    recipe.getTime(),
                    incId, false);

            // Item Input
            int inAmount1 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
            if (recipe.cast != null && inAmount1 > 0) {
                if (recipe.consumesCast()) {
                    List<ChancedIngredientStack> inputMainList1 = recipe.cast.getInputs().stream()
                            .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount1)))
                            .collect(Collectors.toList());
                    if (!inputMainList1.isEmpty()) {
                        machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList1));
                    }
                } else {
                    List<ChancedIngredientStack> inputMainList1 = recipe.cast.getInputs().stream()
                            .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, 1)))
                            .collect(Collectors.toList());
                    if (!inputMainList1.isEmpty()) {
                        RequirementIngredientArray requirementIngredientArray = new RequirementIngredientArray(inputMainList1);
                        requirementIngredientArray.setChance(0);
                        machineRecipe.addRequirement(requirementIngredientArray);
                    }
                }
            }

            // Fluid Input
            int inAmount2 = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_FLUID, IOType.INPUT, recipe.getFluid().amount, false));
            if (inAmount2 > 0) {
                FluidStack input = recipe.getFluid().copy();
                input.amount = inAmount2;
                machineRecipe.addRequirement(new RequirementFluid(IOType.INPUT, recipe.getFluid()));
            }

            // Item Output
            ItemStack output = recipe.getResult();
            int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
            if (outAmount > 0) {
                machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
            }

            machineRecipeList.add(machineRecipe);
            incId++;
        }));

        return machineRecipeList;
    }

}
