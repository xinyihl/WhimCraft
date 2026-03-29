package com.xinyihl.whimcraft.common.mixins.gendustry;

import forestry.api.apiculture.BeeManager;
import forestry.api.genetics.AlleleManager;
import net.bdew.gendustry.machines.imprinter.TileImprinter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = TileImprinter.class, remap = false)
public abstract class TileImprinterQueenMateGenomeMixin {

    @Inject(
            method = "doStart",
            at = @At("HEAD")
    )
    private void whimCraft$ensureBothGenomes(ItemStack bee, CallbackInfo ci) {
        if (bee == null || bee.isEmpty()) {
            return;
        }

        if (AlleleManager.alleleRegistry.getIndividual(bee) == null) {
            return;
        }

        if (!BeeManager.beeRoot.isMated(bee) || BeeManager.beeRoot.isDrone(bee)) {
            return;
        }

        NBTTagCompound tag = bee.getTagCompound();
        if (tag == null) {
            return;
        }

        tag.setTag("Mate", tag.getCompoundTag("Genome"));
    }
}