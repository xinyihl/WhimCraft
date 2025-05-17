package com.xinyihl.whimcraft.common.mixins.ic2;

import com.xinyihl.whimcraft.Configurations;
import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityIronFurnace.class, remap = false)
public abstract class TileEntityIronFurnaceMixin {

    @Inject(
            method = "spawnXP",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injected(EntityPlayer player, double xp, CallbackInfoReturnable<Double> cir){
        if (Configurations.IC2_CONFIG.changeXPOrb) {
            long balls = (long) Math.floor(xp);
            while (balls > 0L) {
                int amount;
                if (balls < 2477L) {
                    amount = EntityXPOrb.getXPSplit((int) balls);
                } else {
                    amount = 2477;
                }
                balls -= amount;
                player.addExperience(amount);
            }
            cir.setReturnValue(xp - Math.floor(xp));
        }
    }
}
