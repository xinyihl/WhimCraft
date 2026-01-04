package com.xinyihl.whimcraft.common.mixins.mmce;

import com.xinyihl.whimcraft.Configurations;
import hellfirepvp.modularmachinery.common.block.prop.ParallelControllerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = ParallelControllerData.class, remap = false)
public abstract class ParallelControllerDataMixin {
    @Shadow
    @Final
    @Mutable
    private static ParallelControllerData[] $VALUES;

    @Invoker(value = "<init>", remap = false)
    private static ParallelControllerData invokeNew(String name, int ordinal, int defaultMaxParallelism) {
        return null;
    }

    @Inject(
            method = "<clinit>",
            at = @At(value = "TAIL"),
            remap = false
    )
    private static void injectNewEnum(CallbackInfo ci) {
        int nextOrdinal = $VALUES.length - 1;
        List<ParallelControllerData> newValues = new ArrayList<>(Arrays.asList($VALUES));
        for (int i = 0; i < Configurations.MMCE_CONFIG.otherParallelController; ++i) {
            newValues.add(invokeNew("WHIMCRAFT_" + i, ++nextOrdinal, 0));
        }
        $VALUES = newValues.toArray(new ParallelControllerData[0]);
    }
}
