package com.xinyihl.whimcraft.common.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class WcPlayer {

    /**
     * 发送标题消息给玩家
     *
     * @param iplayer     目标玩家
     * @param title       主标题（可为 null）
     * @param subTitle    副标题（可为 null）
     * @param fadeInTime  淡入时间（以 tick 为单位）
     * @param displayTime 显示时间（以 tick 为单位）
     * @param fadeOutTime 淡出时间（以 tick 为单位）
     */
    @ZenMethod
    public static void sendTitle(IPlayer iplayer, String title, String subTitle, int fadeInTime, int displayTime, int fadeOutTime) {
        EntityPlayer ep = CraftTweakerMC.getPlayer(iplayer);
        if (!(ep instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) ep;
        SPacketTitle timingPacket = new SPacketTitle(fadeInTime, displayTime, fadeOutTime);
        player.connection.sendPacket(timingPacket);
        if (title != null) {
            SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentString(title));
            player.connection.sendPacket(titlePacket);
        }
        if (subTitle != null) {
            SPacketTitle subTitlePacket = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentString(subTitle));
            player.connection.sendPacket(subTitlePacket);
        }
    }
}
