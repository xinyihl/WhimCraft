package com.xinyihl.whimcraft.client;

import com.xinyihl.whimcraft.common.items.placer.CablePlacer;
import com.xinyihl.whimcraft.common.items.placer.CablePlacerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class CablePreviewRenderer {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player == null) return;
        ItemStack tool = player.getHeldItemMainhand();
        if (!(tool.getItem() instanceof CablePlacer)) return;
        if (!tool.hasTagCompound()) return;
        NBTTagCompound root = tool.getTagCompound().getCompoundTag(CablePlacer.NBT_ROOT);
        List<BlockPos> path = CablePlacerData.getPath(root);
        if (path.isEmpty()) return;

        BlockPos start = CablePlacerData.getStart(root);
        BlockPos end = CablePlacerData.getEnd(root);
        List<BlockPos> waypoints = CablePlacerData.getWaypoints(root);

        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
        GlStateManager.pushMatrix();
        GlStateManager.translate(-px, -py, -pz);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.glLineWidth(5f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        for (BlockPos p : path) {
            double x = p.getX() + 0.5;
            double y = p.getY() + 0.5;
            double z = p.getZ() + 0.5;
            buffer.pos(x, y, z).color(0f, 0.8f, 1f, 0.7f).endVertex();
        }
        tessellator.draw();

        if (start != null) {
            renderMarkerCube(start, 0.0f, 1.0f, 0.0f, 0.8f);
        }
        if (end != null) {
            renderMarkerCube(end, 1.0f, 0.0f, 0.0f, 0.8f);
        }
        for (BlockPos wp : waypoints) {
            renderMarkerCube(wp, 1.0f, 1.0f, 0.0f, 0.8f);
        }

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderMarkerCube(BlockPos pos, float r, float g, float b, float a) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        double size = 0.15;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // 6 个面
        // 下面
        buffer.pos(x - size, y - size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y - size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y - size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y - size, z + size).color(r, g, b, a).endVertex();
        // 上面
        buffer.pos(x - size, y + size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y + size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z - size).color(r, g, b, a).endVertex();
        // 北面
        buffer.pos(x - size, y - size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y + size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y - size, z - size).color(r, g, b, a).endVertex();
        // 南面
        buffer.pos(x - size, y - size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y - size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y + size, z + size).color(r, g, b, a).endVertex();
        // 西面
        buffer.pos(x - size, y - size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y - size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y + size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x - size, y + size, z - size).color(r, g, b, a).endVertex();
        // 东面
        buffer.pos(x + size, y - size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z - size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y + size, z + size).color(r, g, b, a).endVertex();
        buffer.pos(x + size, y - size, z + size).color(r, g, b, a).endVertex();

        tessellator.draw();
    }
}
