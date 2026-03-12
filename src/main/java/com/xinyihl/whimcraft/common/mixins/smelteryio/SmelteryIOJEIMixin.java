package com.xinyihl.whimcraft.common.mixins.smelteryio;

import mctmods.smelteryio.library.util.jei.JEI;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Disable all smelteryio JEI categories
 */
@Mixin(value = JEI.class, remap = false)
public abstract class SmelteryIOJEIMixin {

    /**
     * @author WhimCraft
     * @reason Disable all smelteryio JEI categories
     */
    @Overwrite
    public void registerCategories(IRecipeCategoryRegistration registry) {
        // NO-OP
    }

    /**
     * @author WhimCraft
     * @reason Disable all smelteryio JEI registration
     */
    @Overwrite
    public void register(IModRegistry registry) {
        // NO-OP
    }

    /**
     * @author WhimCraft
     * @reason Disable all smelteryio JEI runtime
     */
    @Overwrite
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        // NO-OP
    }
}
