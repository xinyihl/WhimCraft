package com.xinyihl.whimcraft.common.mixins.botania;

import com.xinyihl.whimcraft.Configurations;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.manapool.ManaPoolRecipeWrapper;

import javax.annotation.Nonnull;

@Mixin(value = ManaPoolRecipeWrapper.class, remap = false)
public abstract class ManaPoolRecipeWrapperMixin implements IRecipeWrapper {

    @Shadow
    @Final
    private int mana;

    /**
     * @author xin_yi_hl
     * @reason jei配方是否显示具体魔力消耗
     */
    @Overwrite
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        GlStateManager.enableAlpha();
        HUDHandler.renderManaBar(20, 50, 255, 0.75F, this.mana, 100000);
        if (Configurations.BOTANIA_CONFIG.showMana) {
            minecraft.fontRenderer.drawString(String.valueOf(this.mana), 20, 41, 0, false);
        }
        GlStateManager.disableAlpha();
    }
}
