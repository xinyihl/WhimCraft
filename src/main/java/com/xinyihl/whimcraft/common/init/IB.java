package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.*;
import com.xinyihl.whimcraft.common.items.LinkCard;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IB {

    public static CreativeTabs CREATIVE_TAB;

    public static Map<Class<? extends TileEntity>, Block> blocks = new HashMap<>();
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

    static {
        if (Mods.MMCE.isLoaded() && (Configurations.MMCE_CONFIG.useShareInfHandler || Mods.TC6.isLoaded())) {
            CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
                public ItemStack createIcon() {
                    return new ItemStack(Items.APPLE);
                }
            };
        }
        if (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler) {
            new LinkCard();
            new BlockShareInfHandler();
        }
        if (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded()) {
            new BlockMEAspectInputBusMMCE();
            new BlockMEAspectOutputBusMMCE();
        }
        if (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded() && Mods.GUGU.isLoaded()) {
            new BlockMEAspectInputBus();
            new BlockMEAspectOutputBus();
        }
    }
}
