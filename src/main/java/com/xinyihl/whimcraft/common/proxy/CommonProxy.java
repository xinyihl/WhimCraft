package com.xinyihl.whimcraft.common.proxy;

import appeng.api.AEApi;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.event.HandlerWorldTick;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.top.TheOneProbe;
import com.xinyihl.whimcraft.common.items.cell.handler.*;
import com.xinyihl.whimcraft.common.utils.RedisClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import redis.clients.jedis.Jedis;

public class CommonProxy {
    public void preInit() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TheOneProbe.class.getName());
    }

    public void postInit() {
        MinecraftForge.EVENT_BUS.register(new HandlerWorldTick());
        if (Mods.AE2.isLoaded()) {
            initAE2();
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void initAE2() {
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityListItemCellHandler());
            AEApi.instance().registries().cell().addCellHandler(new InfinityListFluidCellHandler());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageItemCellHandler());
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageFluidCellHandler());
        }
        if (Mods.MEKENG.isLoaded()) {
            initMEKENG();
        }
    }

    @Optional.Method(modid = "mekeng")
    private void initMEKENG() {
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityListGasCellHandler());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageGasCellHandler());
        }
    }

    public void init() {
        try (Jedis jedis = RedisClient.getJedis()) {
            RedisClient.isOnline = "PONG".equals(jedis.ping());
        } catch (Throwable ignore) {
            //忽略 Redis 报错防止崩溃
        }
    }
}
