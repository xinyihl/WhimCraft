package com.xinyihl.whimcraft.common.init;

import net.minecraftforge.fml.common.Loader;

public enum Mods {
    MMCE("modularmachinery", true),
    GUGU("gugu-utils", true);

    public final String modid;
    private final boolean mixin;
    private final boolean loaded;

    Mods(String modid, boolean isMixin) {
        this.modid = modid;
        this.loaded = Loader.isModLoaded(this.modid);
        this.mixin = isMixin;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isMixin() {
        return mixin;
    }
}
