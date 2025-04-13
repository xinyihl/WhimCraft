package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.init.TheOneProbe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies =
        "required-after:mixinbooter@[8.0,);"
        + "required-after:modularmachinery@[2.0,);"
        + "required-after:thaumcraft@[1.12.2-6.1,);"
        + "required-after:thaumicenergistics@[2.2.0,);"
        + "required-after:appliedenergistics2@[v0.56.0,);"
        //+ "required-after:gugu-utils@[0.8,)"
)
public class WhimCraft {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TheOneProbe.class.getName());
    }
}
