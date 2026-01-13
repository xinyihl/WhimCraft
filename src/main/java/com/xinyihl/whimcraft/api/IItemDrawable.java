package com.xinyihl.whimcraft.api;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemDrawable {
    /**
     * @param instance  RenderItem 对象
     * @param transform 渲染 Transform
     * @param stack     原始待渲染物品
     * @param model     原始待渲染模型(已 Transform 处理)
     */
    @SideOnly(Side.CLIENT)
    default void render(RenderItem instance, ItemCameraTransforms.TransformType transform, ItemStack stack, IBakedModel model) {
        instance.renderItem(stack, model);
    }
}
