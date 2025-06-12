package com.xinyihl.whimcraft.common.api;

import github.kasuminova.mmce.common.tile.MEPatternProvider;

public interface IMEPatternIgnoreParallel {

    MEPatternProvider.WorkModeSetting getWorkMode();

    boolean r$isIgnoreParallel();

    void r$IgnoreParallel();

}