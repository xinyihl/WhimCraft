package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.event.GuiHandler;
import com.xinyihl.whimcraft.common.network.PacketClientToServer;
import com.xinyihl.whimcraft.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = "required-after:mixinbooter;required-after:configanytime;")
public class WhimCraft {
    @Mod.Instance
    public static WhimCraft instance;
    @SidedProxy(clientSide = "com.xinyihl.whimcraft.common.proxy.ClientProxy", serverSide = "com.xinyihl.whimcraft.common.proxy.CommonProxy")
    public static CommonProxy PROXY;
    public SimpleNetworkWrapper networkWrapper;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
        networkWrapper.registerMessage(PacketClientToServer.class, PacketClientToServer.class, 0, Side.SERVER);
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        PROXY.init();
    }
}
