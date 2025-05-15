package com.xinyihl.whimcraft.common.mixins.mobgrindingutils;

import com.xinyihl.whimcraft.Configurations;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.MessageTapParticle;
import mob_grinding_utils.tile.TileEntityTank;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityXPTap.class, remap = false)
public abstract class TileEntityXPTapMixin extends TileEntity implements ITickable {

    @Shadow
    public boolean active;

    @Shadow
    public abstract void spawnXP(World world, BlockPos pos, int xp, TileEntityTank tankTile);

    @Inject(
            method = "update",
            at = @At("HEAD"),
            remap = true,
            cancellable = true
    )
    public void injected(CallbackInfo ci) {
        if (Configurations.MOBUTILS_CONFIG.otherXpSupport) {
            if (!this.getWorld().isRemote && this.active) {
                TileEntity tileentity = this.getWorld().getTileEntity(this.pos.offset((this.getWorld().getBlockState(this.pos).getValue(BlockXPTap.FACING)).getOpposite()));
                if (tileentity instanceof TileEntityTank && ((TileEntityTank) tileentity).tank.getFluidAmount() > 0 && whimCraft$isFluidExp(((TileEntityTank) tileentity).tank.getFluid().getFluid()) && this.getWorld().getTotalWorldTime() % 3L == 0L) {
                    int xpAmount = EntityXPOrbFalling.getXPSplit(Math.min(20, ((TileEntityTank) tileentity).tank.getFluidAmount() / 20));
                    ((TileEntityTank) tileentity).tank.drain(xpAmount * 20, true);
                    this.spawnXP(this.getWorld(), this.pos, xpAmount, (TileEntityTank) tileentity);
                    MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new MessageTapParticle(this.getPos()));
                }
            }
            ci.cancel();
        }
    }

    @Unique
    private boolean whimCraft$isFluidExp(Fluid fluid) {
        return fluid.equals(FluidRegistry.getFluid("xpjuice")) || fluid.equals(FluidRegistry.getFluid("experience")) || fluid.equals(FluidRegistry.getFluid("essence"));
    }
}
