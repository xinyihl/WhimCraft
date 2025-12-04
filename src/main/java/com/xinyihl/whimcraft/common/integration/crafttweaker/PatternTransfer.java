package com.xinyihl.whimcraft.common.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenRegister
@ZenClass("mods.whimcraft.PatternTransfer")
public class PatternTransfer {
    public static Map<String, ItemStack> listInput = new HashMap<>();
    public static Map<String, ItemStack> listOutput = new HashMap<>();

    /**
     * @param recipeCategory 合成表 Category Uid
     * @param input          要添加的 input
     */
    @ZenMethod
    public static void injectInput(String recipeCategory, IItemStack... input) {
        for (IItemStack stack : input) {
            listInput.put(recipeCategory, CraftTweakerMC.getItemStack(stack));
        }
    }

    /**
     * @param recipeCategory 合成表 Category Uid
     * @param output         要添加的 output
     */
    @ZenMethod
    public static void injectOutput(String recipeCategory, IItemStack... output) {
        for (IItemStack stack : output) {
            listOutput.put(recipeCategory, CraftTweakerMC.getItemStack(stack));
        }
    }
}
