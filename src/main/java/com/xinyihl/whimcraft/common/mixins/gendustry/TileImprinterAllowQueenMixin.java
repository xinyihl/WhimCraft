package com.xinyihl.whimcraft.common.mixins.gendustry;

import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.ISpeciesRoot;
import net.bdew.gendustry.items.GeneTemplate;
import net.bdew.gendustry.machines.imprinter.TileImprinter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = TileImprinter.class, remap = false)
public abstract class TileImprinterAllowQueenMixin {

    @Inject(
            method = "isItemValidForSlot",
            at = @At("HEAD"),
            cancellable = true,
            require = 1,
            remap = true
    )
    private void whimCraft$allowQueenImprinting(int index, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (index != 2 || stack == null || stack.isEmpty()) {
            return;
        }

        if (AlleleManager.alleleRegistry.getIndividual(stack) == null) {
            return;
        }

        ItemStack template = ((IInventory) this).getStackInSlot(0);
        if (template.isEmpty()) {
            cir.setReturnValue(true);
            return;
        }

        ISpeciesRoot templateSpeciesRoot = GeneTemplate.getSpecies(template);
        ISpeciesRoot individualSpeciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
        if (templateSpeciesRoot != null && templateSpeciesRoot == individualSpeciesRoot) {
            cir.setReturnValue(true);
        }
    }
}