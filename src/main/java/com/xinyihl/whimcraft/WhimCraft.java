package com.xinyihl.whimcraft;

import com.xinyihl.whimcraft.common.integration.top.TheOneProbe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies =
        "required-after:mixinbooter;"
        + "required-after:jei;"
        + "required-after:modularmachinery;"
        + "required-after:thaumcraft;"
        + "required-after:thaumicenergistics;"
        + "required-after:appliedenergistics2;"
        //+ "required-after:gugu-utils@[0.8,);"
)
public class WhimCraft {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TheOneProbe.class.getName());
    }
}
