package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.items.misc.ItemEncodedPattern;
import com.llamalad7.mixinextras.sugar.Local;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ItemEncodedPattern.class, remap = false)
public abstract class ItemEncodedPatternMixin {
    @Inject(
            method = "addCheckedInformation",
            at = @At(
                    value = "TAIL"
            )
    )
    private void injected(CallbackInfo ci, @Local(name = "stack") ItemStack stack, @Local(name = "lines") List<String> lines) {
        if (Configurations.AEMOD_CONFIG.patternEncoder) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("encoderName")) {
                lines.add(I18n.format("yudreamaddons.tooltip.pattern_encoder.name", tag.getString("encoderName")));
            }
        }
    }
}
