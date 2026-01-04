package com.xinyihl.whimcraft.common.mixins.modtweaker;

import com.blamejared.ModTweaker;
import com.xinyihl.whimcraft.Configurations;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = ModTweaker.class, remap = false)
public abstract class ModTweakerMixin {

    @Final
    @Shadow
    public static List<IAction> LATE_REMOVALS;

    @Final
    @Shadow
    public static List<IAction> LATE_ADDITIONS;

    @Inject(
            method = "preInit",
            at = @At("HEAD")
    )
    public void injectPreInit(FMLPreInitializationEvent e, CallbackInfo ci) {
        if ("preinit".equals(Configurations.MODTWMOD_CONFIG.loadComplete)) {
            whimCraft$apply();
        }
    }

    @Inject(
            method = "init",
            at = @At("HEAD")
    )
    public void injectInit(FMLInitializationEvent e, CallbackInfo ci) {
        if ("init".equals(Configurations.MODTWMOD_CONFIG.loadComplete)) {
            whimCraft$apply();
        }
    }

    @Inject(
            method = "postInit",
            at = @At("HEAD")
    )
    public void injectPostInit(FMLPostInitializationEvent e, CallbackInfo ci) {
        if ("postinit".equals(Configurations.MODTWMOD_CONFIG.loadComplete)) {
            whimCraft$apply();
        }
    }

    @Unique
    public void whimCraft$apply() {
        List<String> needChange = Arrays.asList(Configurations.MODTWMOD_CONFIG.classList);
        List<IAction> A = new ArrayList<>();
        List<IAction> R = new ArrayList<>();
        try {
            for (IAction action : LATE_REMOVALS) {
                if (needChange.contains(action.getClass().getName())) {
                    action.apply();
                    A.add(action);
                }
            }
            for (IAction action : LATE_ADDITIONS) {
                if (needChange.contains(action.getClass().getName())) {
                    action.apply();
                    R.add(action);
                }
            }
        } catch (Exception e) {
            CraftTweakerAPI.logError("Error while applying actions", e);
        }
        LATE_REMOVALS.removeAll(A);
        LATE_ADDITIONS.removeAll(R);
    }
}
