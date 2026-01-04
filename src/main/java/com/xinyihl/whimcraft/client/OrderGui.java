package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.container.ContainerOrder;
import com.xinyihl.whimcraft.common.items.Order;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OrderGui extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/order_gui.png");
    private final ItemStack orderStack;

    public OrderGui(ContainerOrder container, ItemStack stack) {
        super(container);
        this.orderStack = stack;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        if (mouseX > 76 + this.guiLeft && mouseY > 31 + this.guiTop && mouseX < 101 + this.guiLeft && mouseY < 56 + this.guiTop) {
            ItemStack markedItem = Order.getMarkedItem(orderStack);
            this.renderToolTip(markedItem, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("item.whimcraft.order.name");
        this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 0x404040);
        ItemStack markedItem = Order.getMarkedItem(orderStack);
        if (!markedItem.isEmpty()) {
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, markedItem, 80, 35);
            GlStateManager.popMatrix();
        }
    }
}

