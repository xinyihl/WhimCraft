package com.xinyihl.whimcraft.common.event;

import com.xinyihl.whimcraft.Configurations;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerCowMilkAutoFill {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!Configurations.GENERAL_CONFIG.moumou) {
            return;
        }
        if (event.getEntityLiving().world.isRemote) {
            return;
        }
        if (!(event.getEntityLiving() instanceof EntityCow)) {
            return;
        }

        EntityCow cow = (EntityCow) event.getEntityLiving();
        if (cow.isChild() || !cow.onGround) {
            return;
        }

        BlockPos pos = cow.getPosition().down();
        IFluidHandler handler = FluidUtil.getFluidHandler(cow.world, pos, EnumFacing.UP);
        if (handler == null) {
            return;
        }

        FluidStack milk = FluidRegistry.getFluidStack("milk", 100);
        if (milk == null) {
            return;
        }

        handler.fill(milk, true);
    }
}
