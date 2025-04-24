package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies =
        "required-after:mixinbooter;"
        + "required-after:configanytime;"
        + "required-after:jei;"
        + "required-after:modularmachinery;"
        + "required-after:thaumcraft;"
        + "required-after:thaumicenergistics;"
        + "required-after:appliedenergistics2;"
        //+ "required-after:gugu-utils;"
)
public class WhimCraft {
    @SidedProxy(clientSide = "com.xinyihl.whimcraft.common.proxy.ClientProxy", serverSide = "com.xinyihl.whimcraft.common.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
       PROXY.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
    }
}
