package com.xinyihl.whimcraft.common.mixins.ae2fc;

import appeng.api.parts.IPart;
import appeng.container.AEBaseContainer;
import com.glodblock.github.client.container.ContainerFluidPatternTerminal;
import com.llamalad7.mixinextras.sugar.Local;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContainerFluidPatternTerminal.class, remap = false)
public abstract class ContainerFluidPatternTerminalMixin extends AEBaseContainer {
    public ContainerFluidPatternTerminalMixin(InventoryPlayer ip, TileEntity myTile, IPart myPart) {
        super(ip, myTile, myPart);
    }

    @Inject(
            method = "encodeFluidCraftPattern",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setTagCompound(Lnet/minecraft/nbt/NBTTagCompound;)V"
            ),
            remap = true
    )
    private void injected(CallbackInfo ci, @Local(name = "encodedValue") NBTTagCompound encodedValue) {
        if (Configurations.AEMOD_CONFIG.patternEncoder) {
            encodedValue.setString("encoderId", getInventoryPlayer().player.getGameProfile().getId().toString());
            encodedValue.setString("encoderName", getInventoryPlayer().player.getGameProfile().getName());
        }
    }
}
