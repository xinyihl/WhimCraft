package com.xinyihl.whimcraft.common.mixins.extrautils2;

import com.rwtema.extrautils2.items.ItemIngredients;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemIngredients.class, remap = false)
public abstract class ItemIngredientsMixin {
    @Inject(
            method = "getType",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void injected(ItemStack stack, CallbackInfoReturnable<ItemIngredients.Type> cir) {
        if (stack == null) {
            cir.setReturnValue(ItemIngredients.Type.SYMBOL_ERROR);
        }
    }
}
