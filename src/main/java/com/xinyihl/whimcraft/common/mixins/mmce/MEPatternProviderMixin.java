package com.xinyihl.whimcraft.common.mixins.mmce;

import com.xinyihl.whimcraft.common.api.IMEPatternIgnoreParallel;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = MEPatternProvider.class, remap = false)
public abstract class MEPatternProviderMixin extends MEMachineComponent implements IMEPatternIgnoreParallel {
    @Unique
    public boolean novaEngineering_Core$ignoreParallel = true;

    @Shadow
    public abstract MEPatternProvider.WorkModeSetting getWorkMode();

    public boolean r$isIgnoreParallel() {
        return novaEngineering_Core$ignoreParallel;
    }

    public void r$IgnoreParallel() {
        novaEngineering_Core$ignoreParallel = true;
    }
}