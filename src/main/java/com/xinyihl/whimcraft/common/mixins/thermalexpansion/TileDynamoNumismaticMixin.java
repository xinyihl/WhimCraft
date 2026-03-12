package com.xinyihl.whimcraft.common.mixins.thermalexpansion;

import cofh.thermalexpansion.block.dynamo.TileDynamoNumismatic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileDynamoNumismatic.class, remap = false)
public abstract class TileDynamoNumismaticMixin {

    @ModifyConstant(method = "config", constant = @Constant(intValue = 1000))
    private static int modifyBaseEnergy0(int value) {
        return 2000;
    }

    @ModifyConstant(method = "installAugmentToSlot", constant = @Constant(intValue = 4))
    private int modifyBaseEnergy1(int value) {
        return 2;
    }
}
