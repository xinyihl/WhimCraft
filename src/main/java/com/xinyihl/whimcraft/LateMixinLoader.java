package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.init.Mods;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        List<String> mixinConfigs = Arrays.stream(Mods.values()).filter(Mods::isMixin).filter(Mods::isLoaded).map(value -> "mixins/mixins.whimcraft." + value.modid + ".json").collect(Collectors.toList());
        if(Mods.BOTANIA.isLoaded() && Configurations.BOTANIA_CONFIG.linkFlowerToPool){
            mixinConfigs.add("mixins/mixins.whimcraft.botania_pool.json");
        }
        if(Mods.AE2.isLoaded() && Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.isIgnoreParallel) {
            mixinConfigs.add("mixins/mixins.whimcraft.appliedenergistics2_ignoreparallel.json");
            mixinConfigs.add("mixins/mixins.whimcraft.modularmachinery_ignoreparallel.json");
        }
        if(Mods.MODTW.isLoaded() && Configurations.MODTWMOD_CONFIG.loadCompleteMixinEnable) {
            mixinConfigs.add("mixins/mixins.whimcraft.modtweaker.json");
        }
        return mixinConfigs;
    }
}