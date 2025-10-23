package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.*;
import com.xinyihl.whimcraft.common.items.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class IB {

    public static CreativeTabs CREATIVE_TAB;

    public static List<Block> blocks = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();

    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":link_card")
    public static Item linkCard;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockshareinfhandler")
    public static Block blockShareInfHandler;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockshareinfhandler")
    public static Item itemShareInfHandler;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectinputbus")
    public static Block blockMEAspectInputBus;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectinputbus")
    public static Item itemMEAspectInputBus;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectoutputbus")
    public static Block blockMEAspectOutputBus;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectoutputbus")
    public static Item itemMEAspectOutputBus;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectinputbusmmce")
    public static Block blockMEAspectInputBusMMCE;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectinputbusmmce")
    public static Item itemMEAspectInputBusMMCE;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectoutputbusmmce")
    public static Block blockMEAspectOutputBusMMCE;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":blockmeaspectoutputbusmmce")
    public static Item itemMEAspectOutputBusMMCE;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":order")
    public static Item itemOrder;

    static {
        CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
            public ItemStack createIcon() {
                return new ItemStack(items.get(0));
            }
        };
        registerItem(new Order());
        if (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler) {
            registerItem(new LinkCard());
            registerBlock(new BlockShareInfHandler());
        }
        if (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded()) {
            registerBlock(new BlockMEAspectInputBusMMCE());
            registerBlock(new BlockMEAspectOutputBusMMCE());
        }
        if (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded() && Mods.GUGU.isLoaded()) {
            registerBlock(new BlockMEAspectInputBus());
            registerBlock(new BlockMEAspectOutputBus());
        }
    }

    public static void registerBlock(Block block) {
        IB.blocks.add(block);
        registerItem(new MyItemBlock(block));
    }

    public static void registerItem(Item item) {
        IB.items.add(item);
    }
}
