package com.xinyihl.whimcraft;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public class Configurations {

    @Config.Comment("基础设置")
    public static final GeneralConfig GENERAL_CONFIG = new GeneralConfig();

    @Config.Comment("其他设置")
    public static final OtherConfig OTHER_CONFIG = new OtherConfig();

    static {
        ConfigAnytime.register(Configurations.class);
    }

    public static class GeneralConfig {
        @Config.Comment("存储元件存储种类上限(生成世界后不要修改)")
        public int aeTotalTypes = 1024;
        @Config.Comment("皮肤站站点标题")
        public String skinName = "skinName";
        @Config.Comment("额外的并行控制器\n启动一次后前往 mmce 配置文件修改并行数\n需要自行处理模型&贴图（modularmachinery:blockparallelcontroller_whimcraft_[x]）")
        public int otherParallelController = 0;
    }

    public static class OtherConfig {
        @Config.Comment("神秘坩埚配方耗时")
        public int crucibleTime = 100;
        @Config.Comment("神秘源质配方耗时")
        public int smelterTime = 100;
        @Config.Comment("适配器是否使用 gugu 的源质处理系统（ gugu 不支持并行）")
        public boolean useGuguAspect = false;
        @Config.Comment("电路板前缀（eg: contenttweaker:programming_circuit_[x]）")
        public String pcb = "contenttweaker:programming_circuit_";
    }
}
