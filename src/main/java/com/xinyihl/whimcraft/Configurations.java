package com.xinyihl.whimcraft;

import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public class Configurations {
    @Config.Comment("其他设置")
    public static final OtherConfig OTHER_CONFIG = new OtherConfig();

    public static class OtherConfig {
        @Config.Comment("神秘坩埚配方耗时")
        public int crucibleTime = 100;
        @Config.Comment("神秘源质配方耗时")
        public int smelterTime = 100;
        @Config.Comment("适配器是否使用 gugu 的源质处理系统（ gugu 不支持并行）")
        public boolean useGuguAspect = false;
    }
}
