package com.xinyihl.whimcraft.common.mixins.mmce;

import hellfirepvp.modularmachinery.common.block.prop.ParallelControllerData;
import org.spongepowered.asm.mixin.*;
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
    @Final
    @Shadow
    public static ParallelControllerData ULTIMATE = invokeNew("ULTIMATE", $VALUES.length - 1, 1024);
    @Unique
    private static ParallelControllerData WHIMCRAFT_A;
    @Unique
    private static ParallelControllerData WHIMCRAFT_B;

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
        int nextOrdinal = $VALUES.length;

        WHIMCRAFT_A = invokeNew("WHIMCRAFT_A", nextOrdinal, 4096);
        WHIMCRAFT_B = invokeNew("WHIMCRAFT_B", ++nextOrdinal, 16384);

        List<ParallelControllerData> newValues = new ArrayList<>(Arrays.asList($VALUES));

        newValues.set(4, ULTIMATE);
        newValues.add(WHIMCRAFT_A);
        newValues.add(WHIMCRAFT_B);

        $VALUES = newValues.toArray(new ParallelControllerData[0]);
    }
}
