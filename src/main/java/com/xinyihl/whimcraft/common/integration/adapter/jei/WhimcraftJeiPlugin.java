package com.xinyihl.whimcraft.common.integration.adapter.jei;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xinyihl.whimcraft.common.integration.adapter.tc6.AspectCache;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JEIPlugin
public class WhimcraftJeiPlugin implements IModPlugin{

    private static final Logger log = LogManager.getLogger(WhimcraftJeiPlugin.class);
    private static final Gson GSON = new Gson();
    public static final String ASPECT_PATH = "." + File.separator + "config" + File.separator + "thaumcraft_smelter.dat";
    public static Thread aspectCacheThread;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        File aspectFile = new File(ASPECT_PATH);
        if (aspectCacheThread == null && (!aspectFile.exists())) {
            aspectCacheThread = new Thread(() -> {
                Collection<ItemStack> items = registry.getIngredientRegistry().getAllIngredients(registry.getIngredientRegistry().getIngredientType(ItemStack.class));
                log.info("Starting Aspect ItemStack Thread.");
                log.info("Trying to cache " + items.size() + " aspects.");
                this.createAspectsFile(new ArrayList<>(items));
                log.info("Finished Aspect ItemStack Thread.");
            }, "Aspect Cache");
            aspectCacheThread.start();
        }
    }

    public void createAspectsFile(Collection<ItemStack> items) {
        int cachedAmount = 0;
        long lastTimeChecked = System.currentTimeMillis();
        List<AspectCache> aspectCaches = Lists.newArrayList();

        for(ItemStack stack : items) {
            AspectList list = new AspectList(stack);
            if (list.size() > 0) {
                NBTTagCompound tag = new NBTTagCompound();
                list.writeToNBT(tag);
                aspectCaches.add(new AspectCache(stack.serializeNBT().toString(), tag.toString()));
            }
            ++cachedAmount;
            if (lastTimeChecked + 5000L < System.currentTimeMillis()) {
                lastTimeChecked = System.currentTimeMillis();
                log.info("ItemStack Aspect checking at " + 100 * cachedAmount / items.size() + "%");
            }
        }

        try (FileWriter writer = new FileWriter(ASPECT_PATH)) {
            GSON.toJson(aspectCaches, writer);
        } catch (IOException e) {
            log.warn("Failed to open/create aspect caches file");
        }
    }
}
