package com.xinyihl.whimcraft.common.integration.jei;

import com.xinyihl.whimcraft.client.OrderGui;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.adapter.tc6.AspectRequirementUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@JEIPlugin
public class WhimcraftJeiPlugin implements IModPlugin {


    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (Mods.TC6.isLoaded()) {
            AspectRequirementUtil.runCreateAspectsFile(new ArrayList<>(registry.getIngredientRegistry().getAllIngredients(registry.getIngredientRegistry().getIngredientType(ItemStack.class))));
        }
        registry.addGhostIngredientHandler(OrderGui.class, new GhostJEIHandler());
    }
}
