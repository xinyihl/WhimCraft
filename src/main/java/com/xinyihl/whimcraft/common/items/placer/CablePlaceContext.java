package com.xinyihl.whimcraft.common.items.placer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public final class CablePlaceContext {

    public final World world;
    public final EntityPlayer player;
    public final EnumHand hand;
    public final BlockPos target;
    public final ItemStack cableStack;
    public final boolean allowReplace;

    public CablePlaceContext(@Nonnull World world,
                             @Nonnull EntityPlayer player,
                             @Nonnull EnumHand hand,
                             @Nonnull BlockPos target,
                             @Nonnull ItemStack cableStack,
                             boolean allowReplace) {
        this.world = world;
        this.player = player;
        this.hand = hand;
        this.target = target;
        this.cableStack = cableStack;
        this.allowReplace = allowReplace;
    }
}
