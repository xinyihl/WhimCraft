package com.xinyihl.whimcraft.common.proxy;

import com.xinyihl.whimcraft.common.event.GetItemKeyHandler;
import hellfirepvp.modularmachinery.common.block.BlockDynamicColor;
import hellfirepvp.modularmachinery.common.item.ItemDynamicColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import static com.xinyihl.whimcraft.common.init.IB.blocks;
import static com.xinyihl.whimcraft.common.init.IB.items;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new GetItemKeyHandler());
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        for (Block block : blocks.values()) {
            if (block instanceof BlockDynamicColor) {
                BlockDynamicColor blockDynamicColor = (BlockDynamicColor) block;
                blockColors.registerBlockColorHandler(blockDynamicColor::getColorMultiplier, block);
            }
        }
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        for (Item item : items) {
            if (item instanceof ItemDynamicColor) {
                ItemDynamicColor itemDynamicColor = (ItemDynamicColor) item;
                itemColors.registerItemColorHandler(itemDynamicColor::getColorFromItemstack, item);
            }
        }
    }
}
