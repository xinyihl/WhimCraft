package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.xinyihl.whimcraft.Configurations;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.util.IEventHandler;
import github.kasuminova.mmce.common.event.Phase;
import github.kasuminova.mmce.common.event.recipe.RecipeCheckEvent;
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
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copy of AdapterTC6InfusionMatrix with additional runtime research check and tooltip per recipe.
 */
public class AdapterTC6InfusionMatrixResearch extends RecipeAdapter {

    public AdapterTC6InfusionMatrixResearch() {
        super(new ResourceLocation("thaumcraft", "whimcraft_infusion_matrix_research"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName,
                                                      List<RecipeModifier> modifiers,
                                                      List<ComponentRequirement<?, ?>> additionalRequirements,
                                                      Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers,
                                                      List<String> recipeTooltips) {
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

            int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT,
                    recipe.instability == 0 ? Configurations.ADAPTER_CONFIG.infusionMatrixTime : recipe.instability * Configurations.ADAPTER_CONFIG.infusionMatrixTimeMul, false));
            if (inDuration <= 0) {
                return;
            }

            MachineRecipe machineRecipe = createRecipeShell(
                    new ResourceLocation("thaumcraft", "whimcraft_auto_infusion_research" + incId),
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

            Item primordialPearlItem = ItemsTC.primordialPearl;
            if (primordialPearlItem != null) {
                int totalPrimordialPearls = 0;
    
                ItemStack[] inputStacks = recipe.getRecipeInput().getMatchingStacks();
                for (ItemStack inputStack : inputStacks) {
                    if (inputStack != null && !inputStack.isEmpty() && 
                        inputStack.getItem() == primordialPearlItem && inputStack.getMetadata() == 0) {
                        totalPrimordialPearls += inputStack.getCount();
                    }
                }
    
                for (Ingredient component : recipe.getComponents()) {
                    if (component instanceof net.minecraft.item.crafting.Ingredient) {
                        for (ItemStack componentStack : component.getMatchingStacks()) {
                            if (componentStack != null && !componentStack.isEmpty() && 
                                componentStack.getItem() == primordialPearlItem && componentStack.getMetadata() == 0) {
                                totalPrimordialPearls += componentStack.getCount();
                            }
                        }
                    }
                }
    
                if (totalPrimordialPearls > 0) {
                    ItemStack outputPearl = new ItemStack(primordialPearlItem, totalPrimordialPearls, 1);
                    int outAmount = Math.round(RecipeModifier.applyModifiers(
                        modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.OUTPUT, totalPrimordialPearls, false));
        
                    if (outAmount > 0) {
                        machineRecipe.addRequirement(new RequirementItem(
                            IOType.OUTPUT, ItemUtils.copyStackWithSize(outputPearl, outAmount)
                        ));
                        type = false;
                    }
                }
            }

            // Research tooltip and runtime check
            String research = recipe.getResearch();
            if (research != null && !research.isEmpty()) {
                // Tooltip: localized prefix + raw research name for clarity.
                // Add a translatable key first; you can provide a lang like: tooltip.whimcraft.need_research=需要研究
                machineRecipe.addTooltip("tooltip.whimcraft.need_research");
                // And add the research id/name on a second line so players can see which one specifically.
                machineRecipe.addTooltip(research);

                // Runtime check via RecipeCheckEvent
                machineRecipe.addRecipeEventHandler(RecipeCheckEvent.class, event -> {
                    if (!(event instanceof RecipeCheckEvent)) return;
                    RecipeCheckEvent checkEvent = (RecipeCheckEvent) event;
                    if (checkEvent.phase != Phase.START) return;

                    TileMultiblockMachineController controller = checkEvent.getContext().getMachineController();
                    if (controller == null) {
                        checkEvent.setFailed("tooltip.whimcraft.need_research");
                        return;
                    }

                    IPlayer ownerCT = controller.getOwnerIPlayer();
                    if (ownerCT == null) {
                        // 无所有者 -> 阻止
                        checkEvent.setFailed("tooltip.whimcraft.need_research");
                        return;
                    }

                    EntityPlayer mcPlayer = CraftTweakerMC.getPlayer(ownerCT);
                    if (mcPlayer == null) {
                        // 不在线 -> 阻止
                        checkEvent.setFailed("tooltip.whimcraft.need_research");
                        return;
                    }

                    if (!ThaumcraftCapabilities.knowsResearch(mcPlayer, research)) {
                        checkEvent.setFailed("tooltip.whimcraft.need_research");
                    }
                });
            }

            if (type) return;
            machineRecipeList.add(machineRecipe);
            incId++;
        });

        return machineRecipeList;
    }
}
