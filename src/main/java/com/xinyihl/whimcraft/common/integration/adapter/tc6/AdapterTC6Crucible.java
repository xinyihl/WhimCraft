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
import kport.modularmagic.common.crafting.requirement.RequirementAspect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.xinyihl.whimcraft.Configurations.ADAPTER_CONFIG;

public class AdapterTC6Crucible extends RecipeAdapter {
    private static final Logger log = LogManager.getLogger(AdapterTC6Crucible.class);
    public AdapterTC6Crucible() {
        super(new ResourceLocation("thaumcraft", "whimcraft_crucible"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        List<MachineRecipe> machineRecipeList = new ArrayList<>();
        incId = Integer.MAX_VALUE;
        Map<String, Integer> items = new HashMap<>();
        ThaumcraftApi.getCraftingRecipes().forEach((recipeName, tcRecipe) -> {
            if (!(tcRecipe instanceof CrucibleRecipe)) {
                return;
            }
            CrucibleRecipe recipe = (CrucibleRecipe) tcRecipe;
            if (recipe.getCatalyst() == null) {
                return;
            }
            if (recipe.getRecipeOutput() == null || recipe.getRecipeOutput().isEmpty()) {
                return;
            }
            int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, 1, false));
            if (inAmount <= 0) {
                return;
            }
            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("thaumcraft", "whimcraft_auto_crucible" + incId),
                    owningMachineName,
                    ADAPTER_CONFIG.crucibleTime,
                    incId, false);
            // Item Input
            ItemStack[] inputMain = recipe.getCatalyst().getMatchingStacks();
            if("1xitem.nugget@9".equals(inputMain[0].toString())) return;
            List<ChancedIngredientStack> inputMainList = Arrays.stream(inputMain)
                    .map(itemStack -> new ChancedIngredientStack(ItemUtils.copyStackWithSize(itemStack, inAmount)))
                    .collect(Collectors.toList());
            if (!inputMainList.isEmpty()) {
                int i = items.getOrDefault(inputMain[0].toString(), 0);
                Item item = Item.getByNameOrId(ADAPTER_CONFIG.pcb + i);
                if (item == null) {
                    log.fatal("未找到编程电路: " + ADAPTER_CONFIG.pcb + i);
                } else {
                    RequirementItem reqdlb = new RequirementItem(IOType.INPUT, new ItemStack(item));
                    reqdlb.setParallelizeUnaffected(true);
                    machineRecipe.addRequirement(reqdlb);
                    RequirementItem out = new RequirementItem(IOType.OUTPUT, new ItemStack(item));
                    machineRecipe.addRequirement(out);
                }
                items.put(inputMain[0].toString(), i + 1);
                machineRecipe.addRequirement(new RequirementIngredientArray(inputMainList));
            }
            // Aspect Inputs
            recipe.getAspects().aspects.forEach((aspect, amount) -> {
                int inAmounta = Math.round(RecipeModifier.applyModifiers(modifiers, (RequirementTypeAspect) MMRequirements.REQUIREMENT_TYPE_ASPECT, IOType.INPUT, amount, false));
                if (inAmounta <= 0) {
                    return;
                }
                machineRecipe.addRequirement(ADAPTER_CONFIG.useGuguAspect ? com.warmthdawn.mod.gugu_utils.modularmachenary.requirements.RequirementAspect.createInput(inAmounta, aspect) : new RequirementAspect(IOType.INPUT, inAmounta, aspect));
            });
            // Output
            ItemStack output = recipe.getRecipeOutput();
            int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, output.getCount(), false));
            if (outAmount > 0) {
                machineRecipe.addRequirement(new RequirementItem(IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
            }
            machineRecipeList.add(machineRecipe);
            incId--;
        });
        return machineRecipeList;
    }
}
