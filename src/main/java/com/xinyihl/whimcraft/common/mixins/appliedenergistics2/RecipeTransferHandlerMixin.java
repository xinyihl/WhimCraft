package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.helpers.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
        targets = {"appeng.integration.modules.jei.RecipeTransferHandler"},
        remap = false
)
public class RecipeTransferHandlerMixin {
    @Redirect(
            method = "transferRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/helpers/ItemStackHelper;stackToNBT(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/NBTTagCompound;",
                    ordinal = 1
            )
    )
    public NBTTagCompound redirected(ItemStack is) {
        if (is.getCount() > is.getMaxStackSize()) {
            is.setCount(is.getMaxStackSize());
        }
        return ItemStackHelper.stackToNBT(is);
    }
}
