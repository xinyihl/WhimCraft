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
        return Arrays.stream(Mods.values()).filter(Mods::isMixin).filter(Mods::isLoaded).map(value -> "mixins.whimcraft." + value.modid + ".json").collect(Collectors.toList());
    }
}