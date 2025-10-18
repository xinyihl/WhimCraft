package com.xinyihl.whimcraft.common.mixins.minecraft;

import com.llamalad7.mixinextras.sugar.Local;
import com.xinyihl.whimcraft.common.items.Order;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Shadow
    public abstract void renderItem(ItemStack stack, IBakedModel model);

    @Redirect(
            method = "renderItemModelIntoGUI",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"
            )
    )
    public void injected(RenderItem instance, ItemStack stack, IBakedModel model, @Local(name = "x") int x, @Local(name = "y") int y) {
        if (!stack.isEmpty() && stack.getItem() instanceof Order) {
            Minecraft mc = Minecraft.getMinecraft();
            ItemStack markedItem = Order.getMarkedItem(stack);
            // 先绘制被标记的物品
            if (!markedItem.isEmpty()) {
                GlStateManager.pushMatrix();
                mc.getRenderItem().renderItem(markedItem, ItemCameraTransforms.TransformType.GUI);
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5f, 0.5f, 1f);
                GlStateManager.translate(0.5f, -0.5f, 1f);
                renderItem(stack, model);
                GlStateManager.popMatrix();
            } else {
                renderItem(stack, model);
            }
        } else {
            renderItem(stack, model);
        }
    }
}
