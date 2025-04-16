package com.xinyihl.whimcraft.common.proxy;

import com.xinyihl.whimcraft.common.event.GetItemKeyHandler;
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
        MinecraftForge.EVENT_BUS.register(new GetItemKeyHandler());
    }
}
