package com.xinyihl.whimcraft.common.mixins.minecraft;

import com.xinyihl.whimcraft.api.IItemDrawable;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderEntityItem.class)
public abstract class RenderEntityItemMixin {
    @Redirect(
            method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"
            )
    )
    public void injected(RenderItem instance, ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty() && stack.getItem() instanceof IItemDrawable) {
            IItemDrawable itemDrawable = (IItemDrawable) stack.getItem();
            itemDrawable.render(instance, ItemCameraTransforms.TransformType.GROUND, stack, model);
        } else {
            instance.renderItem(stack, model);
        }
    }
}
