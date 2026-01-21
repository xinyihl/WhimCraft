package com.xinyihl.whimcraft.api;

import com.xinyihl.whimcraft.common.items.placer.CablePlaceContext;

public interface ICablePlacer {
    boolean canSelect(net.minecraft.item.ItemStack stack);

    boolean placeCable(CablePlaceContext ctx);
}
