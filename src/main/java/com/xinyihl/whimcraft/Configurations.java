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

    @Config.Comment("更多实用设备设置")
    public static final ExtraUtils2ModConfig EXTRA_CONFIG = new ExtraUtils2ModConfig();

    @Config.Comment("植物魔法设置")
    public static final BotaniaConfig BOTANIA_CONFIG = new BotaniaConfig();

    @Config.Comment("区块清除配置")
    public static final ChunkPurgeConfig CHUNCK_CONFIG = new ChunkPurgeConfig();

    @Config.Comment("刷怪塔实用设备设置")
    public static final MobUtilsConfig MOBUTILS_CONFIG = new MobUtilsConfig();

    @Config.Comment("工业2设置")
    public static final IC2Config IC2_CONFIG = new IC2Config();

    static {
        ConfigAnytime.register(Configurations.class);
    }

    public static class GeneralConfig {
        @Config.Comment("外置登录检查")
        public boolean loginCheckEnable = false;
        @Config.Comment("站点标题")
        public String loginCheckName = "skinName";
        @Config.Comment("多人页面优化")
        public boolean serverListEnable = false;

    }

    public static class MmceConfig {
        @Config.Comment("额外的并行控制器\n启动一次后前往 mmce 配置文件修改并行数\n需要自行处理模型&贴图(modularmachinery:blockparallelcontroller_whimcraft_[x])\n添加格式可打开jar文件查看assets/modularmachinery")
        public int otherParallelController = 0;
        @Config.Comment("样板供应器允许忽略并行处理单元")
        public boolean isIgnoreParallel = false;
        @Config.Comment("是否启用 ME 机械样板供应器库存共享总线")
        public boolean useShareInfHandler = false;
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

    public static class ChunkPurgeConfig{
        @Config.Comment("尝试卸载区块的间隔时间(tick) 必须是大于 0 的整数")
        public int chunkUnloadDelay = 600;
        @Config.Comment("是否启用自动区块清除")
        public boolean enabled = false;
        @Config.Comment("记录从每个维度卸载的区块数，以及卸载区块所花费的时间")
        public boolean debug = false;
        @Config.Comment("每个玩家周围的忽略区块卸载的半径")
        public int pradius = 4;
        @Config.Comment("每个强制加载的区块周围忽略区块卸载的半径。忽略半径之外的所有区块将被强制卸载")
        public int tradius = 5;
        @Config.Comment("由于生物生成行为而加载的区块周围忽略区块卸载的半径")
        public int sradius = 3;
        @Config.Comment("进行检查的维度id")
        public Integer[] dimlist = {-1, 0, 1};
    }

    public static class ASModConfig {
        @Config.Comment("星辉等级(覆盖星辉原版配置文件的设置)")
        public int asLevelCap = 30;
    }

    public static class NatureModConfig {
        @Config.Comment("是否启用自然灵气区块更新")
        public boolean auraChunkUpdateEnable = true;
    }

    public static class ExtraUtils2ModConfig {
        @Config.Comment("彩虹发电机发电量")
        public int rainbowGeneratorEnergy = 2500000;
    }

    public static class BotaniaConfig {
        @Config.Comment("jei配方是否显示具体魔力消耗")
        public boolean showMana = false;

        @Config.Comment("是否允许将产能花直接连接到魔力池")
        public boolean linkFlowerToPool = false;
    }

    public static class MobUtilsConfig {
        @Config.Comment("为XP水龙头添加其他XP流体支持")
        public boolean otherXpSupport = false;
    }

    public static class IC2Config {
        @Config.Comment("修改电炉经验为直接给予玩家，不再掉落为经验球")
        public boolean changeXPOrb = false;
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
        public int gasCell1K = 15;
        public int gasCell4K = 15;
        public int gasCell16K = 15;
        public int gasCell64K = 15;
    }
}
