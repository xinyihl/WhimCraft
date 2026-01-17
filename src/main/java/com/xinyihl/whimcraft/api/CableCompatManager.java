package com.xinyihl.whimcraft.api;

import com.xinyihl.whimcraft.common.items.placer.CablePlaceContext;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CableCompatManager {

    private static final List<ICablePlaceItem> HANDLERS = new ArrayList<>();

    private CableCompatManager() {
    }

    public static void register(@Nonnull ICablePlaceItem handler) {
        HANDLERS.add(handler);
    }

    public static List<ICablePlaceItem> getHandlers() {
        return Collections.unmodifiableList(HANDLERS);
    }

    public static boolean canSelect(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (ICablePlaceItem h : HANDLERS) {
            if (h.canUse() && h.canSelect(stack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean placeCable(@Nonnull CablePlaceContext ctx) {
        for (ICablePlaceItem h : HANDLERS) {
            if (h.canUse() && h.canSelect(ctx.cableStack)) {
                if (h.placeCable(ctx)) {
                    return true;
                }
            }
        }
        return false;
    }
}
