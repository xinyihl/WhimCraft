package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.block.base.IHasTileEntityClass;
import hellfirepvp.modularmachinery.common.block.BlockDynamicColor;
import hellfirepvp.modularmachinery.common.item.ItemDynamicColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

import static com.xinyihl.whimcraft.common.init.IB.blocks;
import static com.xinyihl.whimcraft.common.init.IB.items;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class Registry {
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(items.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blocks.toArray(new Block[0]));
        for (Block block : blocks) {
            if (block instanceof IHasTileEntityClass) {
                GameRegistry.registerTileEntity(((IHasTileEntityClass) block).getTileEntityClass(), new ResourceLocation(Tags.MOD_ID, "tile_" + block.getRegistryName().getPath()));
            }
        }
    }

    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        for (Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }
    }

    @Optional.Method(modid = "modularmachinery")
    public static void initDynamicColor() {
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        for (Block block : blocks) {
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
