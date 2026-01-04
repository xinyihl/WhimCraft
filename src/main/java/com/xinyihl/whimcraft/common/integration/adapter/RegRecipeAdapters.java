package com.xinyihl.whimcraft.common.integration.adapter;

import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.adapter.forestry.AdapterCentrifuge;
import com.xinyihl.whimcraft.common.integration.adapter.forestry.AdapterSqueezer;
import com.xinyihl.whimcraft.common.integration.adapter.ie.AdapterIEArcFurnace;
import com.xinyihl.whimcraft.common.integration.adapter.tc6.*;
import com.xinyihl.whimcraft.common.integration.adapter.tconstruct.AdapterSmelteryBasinCasting;
import com.xinyihl.whimcraft.common.integration.adapter.tconstruct.AdapterSmelteryTableCasting;

import static hellfirepvp.modularmachinery.common.registry.RegistryRecipeAdapters.registerAdapter;

public class RegRecipeAdapters {
    public static void initialize() {
        if (Mods.TC6.isLoaded()) {
            registerAdapter(new AdapterTC6Crucible());
            registerAdapter(new AdapterTC6InfusionMatrix());
            registerAdapter(new AdapterTC6InfusionMatrixResearch());
            registerAdapter(new AdapterTC6Smelter());
            registerAdapter(new AdapterTC6Arcane(true));
            registerAdapter(new AdapterTC6Arcane(false));
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
