package com.xinyihl.whimcraft.common.proxy;

import appeng.api.AEApi;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.api.CableCompatManager;
import com.xinyihl.whimcraft.common.event.HandlerCowMilkAutoFill;
import com.xinyihl.whimcraft.common.event.HandlerWorldTick;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.integration.placer.Ae2CablePlaceHandler;
import com.xinyihl.whimcraft.common.integration.placer.EnderIoCablePlaceHandler;
import com.xinyihl.whimcraft.common.integration.placer.MekanismCablePlaceHandler;
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
        if (Configurations.CHUNCK_CONFIG.enabled) {
            MinecraftForge.EVENT_BUS.register(new HandlerWorldTick());
        }
        if (Configurations.GENERAL_CONFIG.moumou) {
            MinecraftForge.EVENT_BUS.register(new HandlerCowMilkAutoFill());
        }
        if (Mods.AE2.isLoaded()) {
            initAE2();
        }
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

    @Optional.Method(modid = "appliedenergistics2")
    private void initAE2() {
        CableCompatManager.register(new Ae2CablePlaceHandler());
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityListItemCellHandler());
            AEApi.instance().registries().cell().addCellHandler(new InfinityListFluidCellHandler());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageItemCellHandler());
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageFluidCellHandler());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageAllCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageAllCellHandler());
        }
        if (Mods.MEKENG.isLoaded()) {
            initMEKENG();
        }
        if (Mods.TCENERG.isLoaded()) {
            initThaumicEnergistics();
        }
    }

    @Optional.Method(modid = "thaumicenergistics")
    private void initThaumicEnergistics() {
        if (Configurations.AEMOD_CONFIG.infinityListCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityListEssentiaCellHandler());
        }
        if (Configurations.AEMOD_CONFIG.infinityStorageCellEnable) {
            AEApi.instance().registries().cell().addCellHandler(new InfinityStorageEssentiaCellHandler());
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
        if (Configurations.REDIS_IO_CONFIG.enabled) {
            try (Jedis jedis = RedisClient.getJedis()) {
                RedisClient.isOnline = "PONG".equals(jedis.ping());
            } catch (Throwable ignore) {
                //忽略 Redis 报错防止崩溃
            }
        }
    }
}
