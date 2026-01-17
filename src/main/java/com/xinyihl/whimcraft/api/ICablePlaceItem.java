package com.xinyihl.whimcraft.api;

import com.xinyihl.whimcraft.common.items.placer.CablePlaceContext;

public interface ICablePlaceItem {
    boolean canUse();

    boolean canSelect(net.minecraft.item.ItemStack stack);

    boolean placeCable(CablePlaceContext ctx);
}
