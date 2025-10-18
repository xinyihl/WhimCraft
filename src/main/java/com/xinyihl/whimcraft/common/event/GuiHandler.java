package com.xinyihl.whimcraft.common.event;

import com.xinyihl.whimcraft.client.OrderGui;
import com.xinyihl.whimcraft.common.container.ContainerOrder;
import com.xinyihl.whimcraft.common.items.Order;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int ORDER_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ORDER_GUI) {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof Order) {
                return new ContainerOrder(player, heldItem);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ORDER_GUI) {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof Order) {
                return new OrderGui(player, heldItem);
            }
        }
        return null;
    }
}