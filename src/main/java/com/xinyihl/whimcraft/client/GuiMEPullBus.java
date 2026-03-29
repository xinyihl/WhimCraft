package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.container.ContainerMEPullBus;
import com.xinyihl.whimcraft.common.network.PacketClientToServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMEPullBus extends GuiContainer {

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    public GuiMEPullBus(ContainerMEPullBus container) {
        super(container);
        this.xSize = 176;
        this.ySize = 150;
    }

    @Override
    public void initGui() {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(0, x + 137, y + 4, 32, 12, I18n.format("gui." + Tags.MOD_ID + ".me_pull_bus.return")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", "return_items");
            WhimCraft.instance.networkWrapper.sendToServer(
                    new PacketClientToServer(PacketClientToServer.ClientToServer.CLICK_ACTION, tag)
            );
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("tile." + Tags.MOD_ID + ".blockmepullbus.name");
        this.fontRenderer.drawString(title, 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        // Title bar from vanilla chest (176x17)
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, 17);
        // 2 rows of slots from vanilla chest (176x36)
        this.drawTexturedModalRect(x, y + 17, 0, 17, this.xSize, 36);
//        // Cover last column with background color (no 9th slot)
//        drawRect(x + 7 + 8 * 18, y + 17, x + this.xSize - 7, y + 53, 0xFFC6C6C6);
//        // Fill separator area between tile slots and player inv
//        drawRect(x, y + 53, x + this.xSize, y + 54, 0xFFC6C6C6);
        // Player inventory from vanilla chest bottom (176x96)
        this.drawTexturedModalRect(x, y + 53, 0, 126, this.xSize, 96);
    }
}
