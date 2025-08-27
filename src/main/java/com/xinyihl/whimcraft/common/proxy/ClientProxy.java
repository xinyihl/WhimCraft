package com.xinyihl.whimcraft.common.proxy;

import com.xinyihl.whimcraft.common.event.ClientEventHandler;
import com.xinyihl.whimcraft.common.init.Registry;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        Registry.initDynamicColor();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
