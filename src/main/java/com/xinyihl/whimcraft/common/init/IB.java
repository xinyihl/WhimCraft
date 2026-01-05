package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.*;
import com.xinyihl.whimcraft.common.items.LinkCard;
import com.xinyihl.whimcraft.common.items.MyItemBlock;
import com.xinyihl.whimcraft.common.items.Order;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public final class IB {

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
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":redis_input_interface")
    public static Block redisInputInterface;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":redis_input_interface")
    public static Item itemRedisInputInterface;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":redis_output_interface")
    public static Block redisOutputInterface;
    @GameRegistry.ObjectHolder(Tags.MOD_ID + ":redis_output_interface")
    public static Item itemRedisOutputInterface;

    static {
        if(Configurations.GENERAL_CONFIG.orderEnable || Configurations.REDIS_IO_CONFIG.enabled || (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler) || (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded())) {
            CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
                public ItemStack createIcon() {
                    return new ItemStack(items.isEmpty() ? Items.AIR : items.get(0));
                }
            };
        }
        if (Configurations.GENERAL_CONFIG.orderEnable) {
            registerItem(new Order());
        }
        if (Configurations.REDIS_IO_CONFIG.enabled || (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler)) {
            registerItem(new LinkCard());
        }
        if (Configurations.REDIS_IO_CONFIG.enabled) {
            registerBlock(new BlockRedisInputInterface());
            registerBlock(new BlockRedisOutputInterface());
        }
        if (Mods.MMCE.isLoaded()){
            initMmce();
        }
    }

    @Optional.Method(modid = "modularmachinery")
    private static void initMmce() {
        if (Configurations.MMCE_CONFIG.useShareInfHandler) {
            registerBlock(new BlockShareInfHandler());
        }
        if (Mods.AE2.isLoaded() && Mods.TC6.isLoaded()) {
            registerBlock(new BlockMEAspectInputBusMMCE());
            registerBlock(new BlockMEAspectOutputBusMMCE());
        }
        if (Mods.AE2.isLoaded() && Mods.TC6.isLoaded() && Mods.GUGU.isLoaded()) {
            registerBlock(new BlockMEAspectInputBus());
            registerBlock(new BlockMEAspectOutputBus());
        }
    }

    public static void registerBlock(Block block) {
        blocks.add(block);
        registerItem(new MyItemBlock(block));
    }

    public static void registerItem(Item item) {
        items.add(item);
    }
}
