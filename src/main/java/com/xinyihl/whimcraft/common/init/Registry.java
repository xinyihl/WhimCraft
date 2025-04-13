package com.xinyihl.whimcraft.common.init;

import com.xinyihl.whimcraft.Tags;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Map;
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
        event.getRegistry().registerAll(blocks.values().toArray(new Block[0]));
        for (Map.Entry<Class<? extends TileEntity>, Block> entry : blocks.entrySet()) {
            GameRegistry.registerTileEntity(entry.getKey(), new ResourceLocation(Tags.MOD_ID, "tile_" + entry.getValue().getRegistryName().getPath()));
        }
    }

    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event) {
        for (Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }
    }
}
