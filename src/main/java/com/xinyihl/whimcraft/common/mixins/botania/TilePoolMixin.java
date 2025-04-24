package com.xinyihl.whimcraft.common.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.common.block.tile.mana.TilePool;

@Mixin(value = TilePool.class, remap = false)
public abstract class TilePoolMixin implements IManaCollector {

    @Shadow(remap = false)
    public int manaCap;

    @Override
    public void onClientDisplayTick() {

    }

    @Override
    public float getManaYieldMultiplier(IManaBurst iManaBurst) {
        return 1;
    }

    @Override
    public int getMaxMana() {
        return manaCap;
    }
}
