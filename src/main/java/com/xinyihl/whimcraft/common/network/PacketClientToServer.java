package com.xinyihl.whimcraft.common.network;

import com.xinyihl.whimcraft.common.container.ContainerCablePlacer;
import com.xinyihl.whimcraft.common.container.ContainerOrder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketClientToServer implements IMessage, IMessageHandler<PacketClientToServer, IMessage> {
    private String type;
    private NBTTagCompound compound;

    public PacketClientToServer() {
    }

    public PacketClientToServer(ClientToServer type, @Nullable NBTTagCompound compound) {
        this.type = type.name();
        this.compound = compound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = ByteBufUtils.readUTF8String(buf);
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, type);
        ByteBufUtils.writeTag(buf, compound);
    }

    @Override
    public IMessage onMessage(PacketClientToServer message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        player.server.addScheduledTask(() -> {
            switch (ClientToServer.valueOf(message.type)) {
                case CLICK_ACTION: {
                    if (message.compound == null) {
                        break;
                    }
                    String type = message.compound.getString("type");
                    if (player.openContainer instanceof ContainerOrder) {
                        ((ContainerOrder) player.openContainer).onAction(type, message.compound);
                    }
                    if (player.openContainer instanceof ContainerCablePlacer) {
                        ((ContainerCablePlacer) player.openContainer).onAction(type, message.compound);
                    }
                    break;
                }
            }
        });
        return null;
    }

    public enum ClientToServer {
        CLICK_ACTION
    }
}
