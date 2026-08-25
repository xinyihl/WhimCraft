package com.xinyihl.whimcraft.common.proxy;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.api.CableCompatManager;
import com.xinyihl.whimcraft.common.event.HandlerCowMilkAutoFill;
import com.xinyihl.whimcraft.common.event.HandlerWorldTick;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.placer.EnderIoCablePlaceHandler;
import com.xinyihl.whimcraft.common.integration.placer.MekanismCablePlaceHandler;
import com.xinyihl.whimcraft.common.integration.top.TheOneProbe;
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
        if (Configurations.CHUNCK_CONFIG.enabled) {
            MinecraftForge.EVENT_BUS.register(new HandlerWorldTick());
        }
        if (Configurations.GENERAL_CONFIG.moumou) {
            MinecraftForge.EVENT_BUS.register(new HandlerCowMilkAutoFill());
        }
//        if (Mods.AE2.isLoaded()) {
//            initAE2();
//        }
        if (Mods.MEK.isLoaded()) {
            initMekanism();
        }
        if (Mods.ENDIO.isLoaded()) {
            initEnderio();
        }
    }

    @Optional.Method(modid = "enderio")
    private void initEnderio() {
        CableCompatManager.register(new EnderIoCablePlaceHandler());
    }

    @Optional.Method(modid = "mekanism")
    private void initMekanism() {
        CableCompatManager.register(new MekanismCablePlaceHandler());
    }

//    @Optional.Method(modid = "appliedenergistics2")
//    private void initAE2() {
//        //CableCompatManager.register(new Ae2CablePlaceHandler());
//    }

    public void init() {
        if (Configurations.REDIS_IO_CONFIG.enabled) {
            try (Jedis jedis = RedisClient.getJedis()) {
                RedisClient.isOnline = "PONG".equals(jedis.ping());
            } catch (Throwable ignore) {
                //忽略 Redis 报错防止崩溃
            }
        }
    }
}
