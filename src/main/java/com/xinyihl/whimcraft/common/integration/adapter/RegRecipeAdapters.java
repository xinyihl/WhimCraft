package com.xinyihl.whimcraft.common.integration.adapter;

import com.xinyihl.whimcraft.common.integration.adapter.forestry.*;
import com.xinyihl.whimcraft.common.integration.adapter.ie.*;
import com.xinyihl.whimcraft.common.integration.adapter.tc6.*;
import com.xinyihl.whimcraft.common.integration.adapter.tconstruct.*;
import com.xinyihl.whimcraft.common.init.Mods;

import static hellfirepvp.modularmachinery.common.registry.RegistryRecipeAdapters.registerAdapter;

public class RegRecipeAdapters {
    public static void initialize() {
        if (Mods.TC6.isLoaded() && Mods.GUGU.isLoaded()) {
            registerAdapter(new AdapterTC6Crucible());
            registerAdapter(new AdapterTC6InfusionMatrix());
            registerAdapter(new AdapterTC6Smelter());
        }
        if (Mods.IE.isLoaded()) {
            registerAdapter(new AdapterIEArcFurnace());
        }
        if (Mods.TCO.isLoaded()) {
            registerAdapter(new AdapterSmelteryBasinCasting());
            registerAdapter(new AdapterSmelteryTableCasting());
        }
        if (Mods.FORESTRY.isLoaded()) {
            registerAdapter(new AdapterCentrifuge());
            registerAdapter(new AdapterSqueezer());
        }
    }
}
