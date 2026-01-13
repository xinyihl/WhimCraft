package com.xinyihl.whimcraft.common.mixins.minecraft;

import com.llamalad7.mixinextras.sugar.Local;
import com.xinyihl.whimcraft.api.IItemDrawable;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Redirect(
            method = "renderItemModelIntoGUI",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"
            )
    )
    public void injectedGui(RenderItem instance, ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty() && stack.getItem() instanceof IItemDrawable) {
            IItemDrawable itemDrawable = (IItemDrawable) stack.getItem();
            itemDrawable.render(instance, ItemCameraTransforms.TransformType.GUI, stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }

    @Redirect(
            method = "renderItemModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"
            )
    )
    public void injected(RenderItem instance, ItemStack stack, IBakedModel model, @Local(name = "transform") ItemCameraTransforms.TransformType transform) {
        if (!stack.isEmpty() && stack.getItem() instanceof IItemDrawable) {
            IItemDrawable itemDrawable = (IItemDrawable) stack.getItem();
            itemDrawable.render(instance, transform, stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }
}
