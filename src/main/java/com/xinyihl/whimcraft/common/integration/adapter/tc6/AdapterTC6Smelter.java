package com.xinyihl.whimcraft.common.integration.adapter.tc6;

import com.google.gson.Gson;
import crafttweaker.util.IEventHandler;
import github.kasuminova.mmce.common.event.recipe.RecipeEvent;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.RequirementItem;
import hellfirepvp.modularmachinery.common.lib.RequirementTypesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.xinyihl.whimcraft.Configurations.ADAPTER_CONFIG;
import static com.xinyihl.whimcraft.common.integration.adapter.jei.WhimcraftJeiPlugin.ASPECT_PATH;

public class AdapterTC6Smelter extends RecipeAdapter {

    private static final Logger log = LogManager.getLogger(AdapterTC6Smelter.class);
    private static final Map<ItemStack, AspectList> itemAspectList = new HashMap<>();
    private static final Gson GSON = new Gson();

    public AdapterTC6Smelter() {
        super(new ResourceLocation("thaumcraft", "whimcraft_smelter"));
    }

    @Nonnull
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?, ?>> additionalRequirements, Map<Class<?>, List<IEventHandler<RecipeEvent>>> eventHandlers, List<String> recipeTooltips) {
        File file = new File(ASPECT_PATH);
        List<MachineRecipe> machineRecipeList = new ArrayList<>();
        this.optimizedMethod(file);
        if (!itemAspectList.isEmpty()) {
            itemAspectList.forEach((itemStack, aspectList) -> {
                if (itemStack.isEmpty()) {
                    return;
                }
                int inDuration = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_DURATION, IOType.INPUT, ADAPTER_CONFIG.smelterTime, false));
                if (inDuration <= 0) {
                    return;
                }
                MachineRecipe machineRecipe = createRecipeShell(
                        new ResourceLocation("thaumcraft", "whimcraft_auto_smelter" + incId),
                        owningMachineName,
                        inDuration,
                        incId, false);

                int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RequirementTypesMM.REQUIREMENT_ITEM, IOType.INPUT, itemStack.getCount(), false));
                if (inAmount <= 0) {
                    return;
                }
                itemStack.setCount(inAmount);
                machineRecipe.addRequirement(new RequirementItem(IOType.INPUT, itemStack));

                aspectList.aspects.forEach((aspect, integer) -> {
                    int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, AspectRequirementUtil.getRequirementType(), IOType.OUTPUT, integer, false));
                    if (outAmount <= 0) {
                        return;
                    }
                    machineRecipe.addRequirement(AspectRequirementUtil.getRequirement(IOType.OUTPUT, outAmount, aspect));
                });
                machineRecipeList.add(machineRecipe);
                incId++;
            });
        }
        return machineRecipeList;
    }


    private void optimizedMethod(File file) {
        if (!file.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            AspectCache[] aspectCaches = GSON.fromJson(reader, AspectCache[].class);
            for (AspectCache aspectCache : aspectCaches) {
                processAspectCache(aspectCache);
            }
        } catch (IOException e) {
            log.warn("Failed to load aspect caches from file");
        }
    }

    private void processAspectCache(AspectCache aspectCache) {
        AspectList aspectList = new AspectList();
        aspectList.readFromNBT(safeParseNBT(aspectCache.aspects));
        itemAspectList.put(new ItemStack(safeParseNBT(aspectCache.item)), aspectList);
    }

    private NBTTagCompound safeParseNBT(String json) {
        try {
            return JsonToNBT.getTagFromJson(json);
        } catch (NBTException e) {
            return new NBTTagCompound();
        }
    }
}
