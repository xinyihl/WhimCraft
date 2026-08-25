package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.blocks.BlockRedisInputInterface;
import com.xinyihl.whimcraft.common.blocks.BlockRedisOutputInterface;
import com.xinyihl.whimcraft.common.blocks.BlockShareInfHandler;
import com.xinyihl.whimcraft.common.items.Elgoog;
import com.xinyihl.whimcraft.common.items.LinkCard;
import com.xinyihl.whimcraft.common.items.MyItemBlock;
import com.xinyihl.whimcraft.common.items.Order;
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
    public static Item itemInfinityListEssentiaCell;
    public static Item itemInfinityStorageEssentiaCell;
    public static Block blockMEPullBus;
    public static Item itemMEPullBus;

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
        if (Mods.MMCE.isLoaded()) {
            initMmce();
        }
    }

    private static void initTab() {
        if (Configurations.GENERAL_CONFIG.placerEnable || Configurations.GENERAL_CONFIG.elgoogEnable || Configurations.GENERAL_CONFIG.orderEnable || Configurations.REDIS_IO_CONFIG.enabled || (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler) || (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Mods.TC6.isLoaded()) || (Mods.MMCE.isLoaded() && Mods.AE2.isLoaded() && Configurations.AEMOD_CONFIG.mePullBusEnable)) {
            CREATIVE_TAB = new CreativeTabs(Tags.MOD_ID + "_tab") {
                public ItemStack createIcon() {
                    return new ItemStack(items.isEmpty() ? Items.AIR : items.get(0));
                }
            };
        }
    }

    @Optional.Method(modid = "modularmachinery")
    private static void initMmce() {
        if (Configurations.MMCE_CONFIG.useShareInfHandler) {
            blockShareInfHandler = registerBlock(new BlockShareInfHandler());
            itemShareInfHandler = registerItemBlock(blockShareInfHandler);
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
