package com.xinyihl.whimcraft.common.mixins.tinymobfarm;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TileEntityMobFarm.class, remap = false)
public abstract class TileEntityMobFarmMixin extends TileEntity {

    @Inject(
            method = "generateDrops",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
            )
    )
    public void injected(CallbackInfo ci, @Local(name = "drops") List<ItemStack> drops) {
        drops.clear();
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lcn/davidma/tinymobfarm/common/tileentity/TileEntityMobFarm;isWorking()Z",
                    remap = false
            ),
            remap = true
    )
    public boolean injected(TileEntityMobFarm instance) {
        return whimCraft$getInv() != null && instance.isWorking();
    }

    @Unique
    private IItemHandler whimCraft$getInv() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            final TileEntity mobFarm = this.world.getTileEntity(this.pos.offset(facing));
            if (mobFarm != null) {
                final IItemHandler invCap = mobFarm.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (invCap == null) return EmptyHandler.INSTANCE;
                return invCap;
            }
        }
        return null;
    }
}
