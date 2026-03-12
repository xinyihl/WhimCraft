package com.xinyihl.whimcraft.common.mixins.mekanism;

import mekanism.common.item.ItemBlockMachine;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fix crash when NBT is corrupted
 */
@Mixin(value = ItemBlockMachine.class, remap = false)
public abstract class ItemBlockMachineMixin {

    @Redirect(
            method = "getBaseTier",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;getInteger(Ljava/lang/String;)I"
            ),
            remap = true
    )
    private static int fixTierLevel(NBTTagCompound nbt, String key) {
        return Math.max(0, Math.min(4, nbt.getInteger(key)));
    }
}
