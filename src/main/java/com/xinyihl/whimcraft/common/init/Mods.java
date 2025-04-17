package com.xinyihl.whimcraft.common.init;

import net.minecraftforge.fml.common.Loader;

public enum Mods {
    AE2FC("ae2fc", true),
    AE2("appliedenergistics2", true),
    AST("astralsorcery", true),
    GUGU("gugu-utils", true),
    MMCE("modularmachinery", true),
    NAE2("nae2", true),
    NATURE("naturesaura", true),
    TC6("thaumcraft", false),
    IE("immersiveengineering", false),
    TCO("tconstruct", false),
    FORESTRY("forestry", false);

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
