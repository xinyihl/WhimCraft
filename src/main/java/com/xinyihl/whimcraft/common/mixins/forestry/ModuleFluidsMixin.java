package com.xinyihl.whimcraft.common.mixins.forestry;

import forestry.api.recipes.ISqueezerManager;
import forestry.core.ModuleFluids;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Remove ~1500 [Squeezer] (forestry) capsule recipes,
 * when forestry cans and capsules filled/squeezed with every liquid in game.
 */
@Mixin(value = ModuleFluids.class, remap = false)
public abstract class ModuleFluidsMixin {

    @Redirect(
            method = "doInit",
            at = @At(
                    value = "INVOKE",
                    target = "Lforestry/api/recipes/ISqueezerManager;addContainerRecipe(ILnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V"
            )
    )
    private void removeRecipe(ISqueezerManager instance, int i, ItemStack itemStacka, ItemStack itemStackb, float v) {
        // NO-OP
    }
}
