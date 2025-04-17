package com.xinyihl.whimcraft;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public class Configurations {

    @Config.Comment("基础设置")
    public static final GeneralConfig GENERAL_CONFIG = new GeneralConfig();

    @Config.Comment("MMCE适配器设置")
    public static final AdapterConfig ADAPTER_CONFIG = new AdapterConfig();

    @Config.Comment("MMCE设置")
    public static final MmceConfig MMCE_CONFIG = new MmceConfig();

    @Config.Comment("AE设置")
    public static final AEModConfig AEMOD_CONFIG = new AEModConfig();

    @Config.Comment("星辉设置")
    public static final ASModConfig ASMOD_CONFIG = new ASModConfig();

    @Config.Comment("自然灵气设置")
    public static final NatureModConfig NATURE_CONFIG = new NatureModConfig();

    static {
        ConfigAnytime.register(Configurations.class);
    }

    public static class GeneralConfig {
        @Config.Comment("外置登录检查")
        public boolean loginCheckEnable = false;
        @Config.Comment("站点标题")
        public String loginCheckName = "skinName";
    }

    public static class MmceConfig {
        @Config.Comment("额外的并行控制器\n启动一次后前往 mmce 配置文件修改并行数\n需要自行处理模型&贴图(modularmachinery:blockparallelcontroller_whimcraft_[x])")
        public int otherParallelController = 0;
    }

    public static class AdapterConfig {
        @Config.Comment("神秘坩埚配方耗时")
        public int crucibleTime = 100;
        @Config.Comment("神秘源质配方耗时")
        public int smelterTime = 100;
        @Config.Comment("适配器是否使用 gugu 的源质处理系统(gugu 不支持并行)")
        public boolean useGuguAspect = false;
        @Config.Comment("电路板前缀(eg: contenttweaker:programming_circuit_[x])")
        public String pcb = "contenttweaker:programming_circuit_";
    }

    public static class AEModConfig {
        @Config.Comment("存储元件存储种类上限(生成世界后不要修改)")
        public int aeTotalTypes = 63;
        @Config.Comment("编码样板显示由谁编码")
        public boolean patternEncoder = false;
        @Config.Comment("存储种类(设置值应小于等于 aeTotalTypes)")
        public AECellConfig AECellConfig = new AECellConfig();
    }

    public static class ASModConfig {
        @Config.Comment("星辉等级(覆盖星辉原版配置文件的设置)")
        public int asLevelCap = 30;
    }

    public static class NatureModConfig {
        @Config.Comment("是否启用自然灵气区块更新")
        public boolean auraChunkUpdateEnable = true;
    }

    public static class AECellConfig {
        public int cell1K = 63;
        public int cell4K = 63;
        public int cell16K = 63;
        public int cell64K = 63;
        public int cell256K = 63;
        public int cell1024K = 63;
        public int cell4096K = 63;
        public int cell16384K = 63;
        public int fluidCell1K = 5;
        public int fluidCell4K = 5;
        public int fluidCell16K = 5;
        public int fluidCell64K = 5;
        public int fluidCell256K = 5;
        public int fluidCell1024K = 5;
        public int fluidCell4096K = 5;
        public int fluidCell16384K = 5;
    }
}
