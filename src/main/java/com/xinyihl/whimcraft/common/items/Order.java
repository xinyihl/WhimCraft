package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.api.IItemDrawable;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class Order extends Item implements IItemDrawable {

    public Order(){
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID,"order"));
        this.setTranslationKey(Tags.MOD_ID + ".order");
    }


    // 获取标记的物品
    public static ItemStack getMarkedItem(ItemStack orderStack) {
        if (orderStack.hasTagCompound() && orderStack.getTagCompound().hasKey("MarkedItem")) {
            return new ItemStack(orderStack.getTagCompound().getCompoundTag("MarkedItem"));
        }
        return ItemStack.EMPTY;
    }

    // 设置标记的物品
    public static void setMarkedItem(ItemStack orderStack, ItemStack markedItem) {
        if (!orderStack.hasTagCompound()) {
            orderStack.setTagCompound(new NBTTagCompound());
        }

        if (!markedItem.isEmpty()) {
            NBTTagCompound itemTag = new NBTTagCompound();
            markedItem.writeToNBT(itemTag);
            orderStack.getTagCompound().setTag("MarkedItem", itemTag);
        } else {
            orderStack.getTagCompound().removeTag("MarkedItem");
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            player.openGui(WhimCraft.instance, GuiHandler.ORDER_GUI, world,
                    (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return ActionResult.newResult(net.minecraft.util.EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flag) {
        ItemStack markedItem = getMarkedItem(stack);
        if (!markedItem.isEmpty()) {
            tooltip.add("标记: " + markedItem.getDisplayName());
        } else {
            tooltip.add("右键点击标记物品");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(RenderItem instance, ItemCameraTransforms.TransformType transform, ItemStack stack, IBakedModel model) {
        ItemStack markedItem = Order.getMarkedItem(stack);
        if (!markedItem.isEmpty() && transform == ItemCameraTransforms.TransformType.GUI) {
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderItem().renderItem(markedItem, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 1f);
            GlStateManager.translate(0.5f, -0.5f, 1f);
            instance.renderItem(stack, model);
            GlStateManager.popMatrix();
            return;
        }
        instance.renderItem(stack, model);
    }
}
