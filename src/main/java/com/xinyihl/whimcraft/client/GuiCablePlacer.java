package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.container.ContainerCablePlacer;
import com.xinyihl.whimcraft.common.items.placer.CablePlacer;
import com.xinyihl.whimcraft.common.network.PacketClientToServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCablePlacer extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/order_gui.png");

    private int page = 0; // 0: 选择页 1: 配置页

    private GuiButton btnPage;
    private GuiButton btnAllowReplace;
    private GuiButton btnClear;

    public GuiCablePlacer(ContainerCablePlacer container) {
        super(container);
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int x = this.guiLeft;
        int y = this.guiTop;
        btnPage = new GuiButton(0, x + 7, y + 6, 60, 20, "...");
        btnAllowReplace = new GuiButton(2, x + 7, y + 30, 120, 20, "...");
        btnClear = new GuiButton(3, x + 7, y + 54, 120, 20, I18n.format("gui.whimcraft.cable_placer.clear"));
        this.buttonList.add(btnPage);
        this.buttonList.add(btnAllowReplace);
        this.buttonList.add(btnClear);
        refreshButtons();
    }

    private ItemStack getTool() {
        return this.mc.player.getHeldItemMainhand();
    }

    private void refreshButtons() {
        btnPage.displayString = page == 0 ? I18n.format("gui.whimcraft.cable_placer.page.config") : I18n.format("gui.whimcraft.cable_placer.page.select");
        boolean isTool = getTool().getItem() instanceof CablePlacer;
        btnAllowReplace.visible = page == 1 && isTool;
        btnClear.visible = page == 1 && isTool;
        if (isTool) {
            btnAllowReplace.displayString = I18n.format("gui.whimcraft.cable_placer.allow_replace", CablePlacer.getOptAllowReplace(getTool()) ? I18n.format("gui.whimcraft.on") : I18n.format("gui.whimcraft.off"));
        }
        btnAllowReplace.enabled = page == 1;
        btnClear.enabled = page == 1;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!(getTool().getItem() instanceof CablePlacer)) {
            return;
        }
        if (button.id == 0) {
            page = 1 - page;
            refreshButtons();
            return;
        }
        if (button.id == 2) {
            boolean next = !CablePlacer.getOptAllowReplace(getTool());
            sendToggle("toggle_allow_replace", next);
            CablePlacer.setOptAllowReplace(getTool(), next);
            refreshButtons();
            return;
        }
        if (button.id == 3) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", "clear_points");
            WhimCraft.instance.networkWrapper.sendToServer(new PacketClientToServer(PacketClientToServer.ClientToServer.CLICK_ACTION, tag));
            CablePlacer.clearAll(getTool());
            refreshButtons();
        }
    }

    private void sendToggle(String type, boolean value) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", type);
        tag.setBoolean("value", value);
        WhimCraft.instance.networkWrapper.sendToServer(new PacketClientToServer(PacketClientToServer.ClientToServer.CLICK_ACTION, tag));
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("item.whimcraft.cable_placer.name");
        this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 0x404040);
        if (page == 0) {
            this.fontRenderer.drawString(I18n.format("gui.whimcraft.cable_placer.select_hint"), 8, 24, 0x404040);
            ItemStack tool = getTool();
            if (tool.getItem() instanceof CablePlacer) {
                ItemStack cable = CablePlacer.getCableStack(tool);
                if (!cable.isEmpty()) {
                    GlStateManager.pushMatrix();
                    RenderHelper.enableGUIStandardItemLighting();
                    this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, cable, 80, 35);
                    GlStateManager.popMatrix();
                }
            }
        } else {
            this.fontRenderer.drawString(I18n.format("gui.whimcraft.cable_placer.config_title"), 8, 24, 0x404040);
        }
    }
}
