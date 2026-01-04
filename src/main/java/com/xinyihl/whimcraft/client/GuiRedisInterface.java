package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.container.ContainerRedisInterface;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiRedisInterface extends GuiContainer {

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    private final boolean isInput;
    private final int rows;

    public GuiRedisInterface(ContainerRedisInterface inventorySlotsIn, boolean isInput) {
        super(inventorySlotsIn);
        this.isInput = isInput;
        this.rows = 5;
        this.ySize = 114 + this.rows * 18;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String titleKey = isInput ? "tile." + Tags.MOD_ID + ".redis_input_interface.name" : "tile." + Tags.MOD_ID + ".redis_output_interface.name";
        String title = I18n.format(titleKey);
        this.fontRenderer.drawString(title, 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int upper = this.rows * 18 + 17;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, upper);
        this.drawTexturedModalRect(i, j + upper, 0, 126, this.xSize, 96);
    }
}
