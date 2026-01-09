package com.xinyihl.whimcraft.common.proxy;

import appeng.api.AEApi;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.event.HandlerWorldTick;
import com.xinyihl.whimcraft.common.handler.InfinityListCellHandler;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.top.TheOneProbe;
import com.xinyihl.whimcraft.common.redis.RedisClient;
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
        if (Mods.AE2.isLoaded() && Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            initAE2();
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private void initAE2() {
        AEApi.instance().registries().cell().addCellHandler(new InfinityListCellHandler());
    }

    public void init() {
        try (Jedis jedis = RedisClient.getJedis()) {
            RedisClient.isOnline = "PONG".equals(jedis.ping());
        } catch (Exception ignore) {
            //忽略 Redis 报错防止崩溃
        }
    }
}
