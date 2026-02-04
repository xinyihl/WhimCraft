package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.integration.modules.jei.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class JEIPluginMixin {
    @Redirect(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/recipe/transfer/IRecipeTransferRegistry;addRecipeTransferHandler(Lmezz/jei/api/recipe/transfer/IRecipeTransferHandler;Ljava/lang/String;)V"
            )
    )
    public void redirected(IRecipeTransferRegistry instance, IRecipeTransferHandler<?> iRecipeTransferHandler, String string) {
        instance.addUniversalRecipeTransferHandler(iRecipeTransferHandler);
    }
}
