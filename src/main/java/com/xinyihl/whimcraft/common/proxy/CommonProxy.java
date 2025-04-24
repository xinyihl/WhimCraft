package com.xinyihl.whimcraft.common.proxy;

import com.xinyihl.whimcraft.common.event.HandlerWorldTick;
import com.xinyihl.whimcraft.common.integration.top.TheOneProbe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CommonProxy {
    public void preInit() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TheOneProbe.class.getName());
    }

    public void postInit()
    {
        MinecraftForge.EVENT_BUS.register(new HandlerWorldTick());
    }

    public void init() {
    }
}
