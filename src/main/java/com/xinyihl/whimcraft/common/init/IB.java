package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.*;
import com.xinyihl.whimcraft.common.items.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IB {

    public static CreativeTabs CREATIVE_TAB;

    public static Map<Class<? extends TileEntity>, Block> blocks = new HashMap<>();
    public static List<Item> items = new ArrayList<>();

    public static Item linkCard;

    public static Block blockShareInfHandler;
    public static Item itemShareInfHandler;

    public static Block blockMEAspectInputBus;
    public static Block blockMEAspectOutputBus;
    public static Item itemMEAspectInputBus;
    public static Item itemMEAspectOutputBus;

    public static Block blockMEAspectInputBusMMCE;
    public static Block blockMEAspectOutputBusMMCE;
    public static Item itemMEAspectInputBusMMCE;
    public static Item itemMEAspectOutputBusMMCE;

    static {
        if (Mods.MMCE.isLoaded()) {
            initMmce();
            if(Mods.TC6.isLoaded()) {
                initTc();
            }
        }
        if (Mods.GUGU.isLoaded()) {
            initGugu();
        }
        if (!items.isEmpty()) {
            CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
                public ItemStack createIcon() {
                    return new ItemStack(items.get(0));
                }
            };
        }
    }

    @Optional.Method(modid = "gugu-utils")
    public static void initGugu() {
        blockMEAspectInputBus = new BlockMEAspectInputBus();
        blockMEAspectOutputBus = new BlockMEAspectOutputBus();
        itemMEAspectInputBus = new MyItemBlock(blockMEAspectInputBus);
        itemMEAspectOutputBus = new MyItemBlock(blockMEAspectOutputBus);
    }

    @Optional.Method(modid = "modularmachinery")
    public static void initMmce() {
        linkCard = new LinkCard();
        blockShareInfHandler  = new BlockShareInfHandler();
        itemShareInfHandler = new MyItemBlock(blockShareInfHandler);
    }

    @Optional.Method(modid = "thaumcraft")
    public static void initTc() {
        blockMEAspectInputBusMMCE = new BlockMEAspectInputBusMMCE();
        blockMEAspectOutputBusMMCE = new BlockMEAspectOutputBusMMCE();
        itemMEAspectInputBusMMCE = new MyItemBlock(blockMEAspectInputBusMMCE);
        itemMEAspectOutputBusMMCE = new MyItemBlock(blockMEAspectOutputBusMMCE);
    }
}
