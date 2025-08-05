package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.init.Mixins;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Mixins.getMixinConfigs();
    }
}