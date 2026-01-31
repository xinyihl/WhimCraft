package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.blocks.*;
import com.xinyihl.whimcraft.common.items.Elgoog;
import com.xinyihl.whimcraft.common.items.LinkCard;
import com.xinyihl.whimcraft.common.items.MyItemBlock;
import com.xinyihl.whimcraft.common.items.Order;
import com.xinyihl.whimcraft.common.items.cell.*;
import com.xinyihl.whimcraft.common.items.placer.CablePlacer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;

public final class IB {

    public static CreativeTabs CREATIVE_TAB;

    public static List<Block> blocks = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();

    public static Item itemElgoog;
    public static Item itemOrder;
    public static Item itemCablePlacer;
    public static Item itemLinkCard;
    public static Block blockShareInfHandler;
    public static Item itemShareInfHandler;
    public static Block blockMEAspectInputBus;
    public static Item itemMEAspectInputBus;
    public static Block blockMEAspectOutputBus;
    public static Item itemMEAspectOutputBus;
    public static Block blockMEAspectInputBusMMCE;
    public static Item itemMEAspectInputBusMMCE;
    public static Block blockMEAspectOutputBusMMCE;
    public static Item itemMEAspectOutputBusMMCE;
    public static Block blockRedisInputInterface;
    public static Item itemRedisInputInterface;
    public static Block blockRedisOutputInterface;
    public static Item itemRedisOutputInterface;
    public static Item itemInfinityListFluidCell;
    public static Item itemInfinityStorageFluidCell;
    public static Item itemInfinityListItemCell;
    public static Item itemInfinityStorageItemCell;
    public static Item itemInfinityListGasCell;
    public static Item itemInfinityStorageGasCell;
    public static Item itemInfinityStorageAllCell;

    static {
        initTab();
        if (Configurations.GENERAL_CONFIG.elgoogEnable) {
            itemElgoog = registerItem(new Elgoog());
        }
        if (Configurations.GENERAL_CONFIG.orderEnable) {
            itemOrder = registerItem(new Order());
        }
        if (Configurations.GENERAL_CONFIG.placerEnable) {
            itemCablePlacer = registerItem(new CablePlacer());
        }
        if (Configurations.REDIS_IO_CONFIG.enabled || (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler)) {
            itemLinkCard = registerItem(new LinkCard());
        }
        if (Configurations.REDIS_IO_CONFIG.enabled) {
            blockRedisInputInterface = registerBlock(new BlockRedisInputInterface());
            itemRedisInputInterface = registerItemBlock(blockRedisInputInterface);
            blockRedisOutputInterface = registerBlock(new BlockRedisOutputInterface());
            itemRedisOutputInterface = registerItemBlock(blockRedisOutputInterface);
        }
        if (Mods.AE2.isLoaded()) {
            initAE2();
        }
        if (Mods.MMCE.isLoaded()) {
            initMmce();
        }
    }

    private static void initTab() {
        if (Configurations.GENERAL_CONFIG.placerEnable || Configurations.GENERAL_CONFIG.elgoogEnable || Configurations.GENERAL_CONFIG.orderEnable || Configurations.REDIS_IO_CONFIG.enabled || (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler) || (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded())) {
            CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
                public ItemStack createIcon() {
                    return new ItemStack(items.isEmpty() ? Items.AIR : items.get(0));
                }
            };
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void initAE2() {
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            itemInfinityListFluidCell = registerItem(new InfinityListFluidCell());
            itemInfinityListItemCell = registerItem(new InfinityListItemCell());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            itemInfinityStorageFluidCell = registerItem(new InfinityStorageFluidCell());
            itemInfinityStorageItemCell = registerItem(new InfinityStorageItemCell());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageAllCellEnable) {
            itemInfinityStorageAllCell = registerItem(new InfinityStorageAllCell());
        }
        if (Mods.MEKENG.isLoaded()) {
            initMEKENG();
        }
    }

    @Optional.Method(modid = "mekeng")
    private static void initMEKENG() {
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            itemInfinityListGasCell = registerItem(new InfinityListGasCell());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            itemInfinityStorageGasCell = registerItem(new InfinityStorageGasCell());
        }
    }

    @Optional.Method(modid = "modularmachinery")
    private static void initMmce() {
        if (Configurations.MMCE_CONFIG.useShareInfHandler) {
            blockShareInfHandler = registerBlock(new BlockShareInfHandler());
            itemShareInfHandler = registerItemBlock(blockShareInfHandler);
        }
        if (Mods.AE2.isLoaded() && Mods.TC6.isLoaded()) {
            blockMEAspectInputBusMMCE = registerBlock(new BlockMEAspectInputBusMMCE());
            itemMEAspectInputBusMMCE = registerItemBlock(blockMEAspectInputBusMMCE);
            blockMEAspectOutputBusMMCE = registerBlock(new BlockMEAspectOutputBusMMCE());
            itemMEAspectOutputBusMMCE = registerItemBlock(blockMEAspectOutputBusMMCE);
        }
        if (Mods.AE2.isLoaded() && Mods.TC6.isLoaded() && Mods.GUGU.isLoaded()) {
            blockMEAspectInputBus = registerBlock(new BlockMEAspectInputBus());
            itemMEAspectInputBus = registerItemBlock(blockMEAspectInputBus);
            blockMEAspectOutputBus = registerBlock(new BlockMEAspectOutputBus());
            itemMEAspectOutputBus = registerItemBlock(blockMEAspectOutputBus);
        }
    }

    private static Block registerBlock(Block block) {
        blocks.add(block);
        return block;
    }

    private static Item registerItem(Item item) {
        items.add(item);
        return item;
    }

    private static Item registerItemBlock(Block block) {
        Item item = new MyItemBlock(block);
        items.add(item);
        return item;
    }
}
