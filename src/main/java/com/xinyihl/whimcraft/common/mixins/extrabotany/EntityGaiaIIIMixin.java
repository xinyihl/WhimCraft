package com.xinyihl.whimcraft.common.mixins.extrabotany;

import com.meteor.extrabotany.common.entity.gaia.EntityGaiaIII;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EntityGaiaIII.class, remap = false)
public abstract class EntityGaiaIIIMixin {

    @Shadow
    public abstract BlockPos getSource();

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/meteor/extrabotany/common/entity/gaia/EntityGaiaIII;smashCheatyBlocks()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void whimCraft$preventEmptyPlayersCrash(CallbackInfo ci) {
        EntityGaiaIII self = (EntityGaiaIII) (Object) this;
        BlockPos source = this.getSource();
        List<EntityPlayer> players = self.world.getEntitiesWithinAABB(
                EntityPlayer.class,
                new AxisAlignedBB(
                        source.getX() + 0.5D - 15.0D,
                        source.getY() + 0.5D - 15.0D,
                        source.getZ() + 0.5D - 15.0D,
                        source.getX() + 0.5D + 15.0D,
                        source.getY() + 0.5D + 30.0D,
                        source.getZ() + 0.5D + 15.0D
                )
        );

        if (!players.isEmpty()) {
            return;
        }

        if (!self.world.playerEntities.isEmpty()) {
            self.setDead();
        }
        ci.cancel();
    }
}