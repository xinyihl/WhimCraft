package com.xinyihl.whimcraft.common.event;

import com.xinyihl.whimcraft.client.GuiRedisInterface;
import com.xinyihl.whimcraft.client.OrderGui;
import com.xinyihl.whimcraft.common.container.ContainerOrder;
import com.xinyihl.whimcraft.common.container.ContainerRedisInterface;
import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.tile.TileRedisInputInterface;
import com.xinyihl.whimcraft.common.tile.TileRedisOutputInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int ORDER_GUI = 1;
    public static final int REDIS_INPUT_GUI = 2;
    public static final int REDIS_OUTPUT_GUI = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ORDER_GUI) {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof Order) {
                return new ContainerOrder(player);
            }
        }
        if (ID == REDIS_INPUT_GUI) {
            TileEntity te = world.getTileEntity(new net.minecraft.util.math.BlockPos(x, y, z));
            if (te instanceof TileRedisInputInterface) {
                return new ContainerRedisInterface(player.inventory, (TileRedisInputInterface) te, true);
            }

        }
        if (ID == REDIS_OUTPUT_GUI) {
            TileEntity te = world.getTileEntity(new net.minecraft.util.math.BlockPos(x, y, z));
            if (te instanceof TileRedisOutputInterface) {
                return new ContainerRedisInterface(player.inventory, (TileRedisOutputInterface) te, false);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ORDER_GUI) {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof Order) {
                return new OrderGui(new ContainerOrder(player), heldItem);
            }
        }
        if (ID == REDIS_INPUT_GUI) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileRedisInputInterface) {
                return new GuiRedisInterface(new ContainerRedisInterface(player.inventory, (TileRedisInputInterface) te, true), true);
            }
        }
        if (ID == REDIS_OUTPUT_GUI) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileRedisOutputInterface) {
                return new GuiRedisInterface(new ContainerRedisInterface(player.inventory, (TileRedisOutputInterface) te, false), false);
            }
        }
        return null;
    }
}