package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.BlockMEAspectInputBus;
import com.xinyihl.whimcraft.common.block.BlockMEAspectInputBusMMCE;
import com.xinyihl.whimcraft.common.block.BlockMEAspectOutputBus;
import com.xinyihl.whimcraft.common.block.BlockMEAspectOutputBusMMCE;
import com.xinyihl.whimcraft.common.item.MyItemBlock;
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
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
        @Override
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(items.get(0));
        }
    };
    public static Map<Class<? extends TileEntity>, Block> blocks = new HashMap<>();
    public static List<Item> items = new ArrayList<>();

    public static BlockMEAspectInputBus blockMEAspectInputBus;
    public static BlockMEAspectOutputBus blockMEAspectOutputBus;
    public static Item itemMEAspectInputBus;
    public static Item itemMEAspectOutputBus;

    public static BlockMEAspectInputBusMMCE blockMEAspectInputBusMMCE;
    public static BlockMEAspectOutputBusMMCE blockMEAspectOutputBusMMCE;
    public static Item itemMEAspectInputBusMMCE;
    public static Item itemMEAspectOutputBusMMCE;

    static {
        if (Mods.MMCE.isLoaded()) {
            initMmce();
        }
        if (Mods.GUGU.isLoaded()) {
            initGugu();
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
        blockMEAspectInputBusMMCE = new BlockMEAspectInputBusMMCE();
        blockMEAspectOutputBusMMCE = new BlockMEAspectOutputBusMMCE();
        itemMEAspectInputBus = new MyItemBlock(blockMEAspectInputBusMMCE);
        itemMEAspectOutputBus = new MyItemBlock(blockMEAspectOutputBusMMCE);
    }
}
