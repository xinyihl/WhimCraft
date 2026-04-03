package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.container.ContainerMEPullBus;
import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMEPullBus extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("modularmachinery", "textures/gui/meiteminputbus.png");
    private final ContainerMEPullBus container;

    public GuiMEPullBus(ContainerMEPullBus container) {
        super(container);
        this.container = container;
        this.xSize = 176;
        this.ySize = 204;
    }

    @Override
    public void initGui() {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(0, x + 116, y + 7, 52, 12, I18n.format("gui." + Tags.MOD_ID + ".me_pull_bus.return")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Utils.sendAction("return_items");
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("tile." + Tags.MOD_ID + ".blockmepullbus.name");
        this.fontRenderer.drawString(title, 8, 6, 0x404040);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 93, 0x404040);

        RenderHelper.enableGUIStandardItemLighting();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int slotIndex = row * 4 + col;
                Slot slot = container.getSlot(slotIndex);
                ItemStack stack = slot.getStack();
                if (stack.isEmpty()) continue;
                int rx = 8 + col * 18 + 1;
                int ry = 35 + row * 18 + 1;
                ItemStack display = stack.copy();
                display.setCount(1);
                this.itemRender.renderItemAndEffectIntoGUI(display, rx, ry);
            }
        }
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
