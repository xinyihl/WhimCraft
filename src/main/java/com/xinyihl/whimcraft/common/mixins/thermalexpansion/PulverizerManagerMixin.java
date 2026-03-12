package com.xinyihl.whimcraft.common.mixins.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.PulverizerManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PulverizerManager.class, remap = false)
public abstract class PulverizerManagerMixin {

    @Redirect(
            method = "initialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lcofh/thermalexpansion/util/managers/machine/PulverizerManager;addRecycleRecipe(ILnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)V"
            )
    )
    private static void removeThermalTools(int energy, ItemStack item, ItemStack output, int outputSize) {
        if (item != null && !item.isEmpty() && item.getItem().getRegistryName() != null
                && "minecraft".equals(item.getItem().getRegistryName().getNamespace())) {
            PulverizerManager.addRecycleRecipe(energy, item, output, outputSize);
        }
    }
}
