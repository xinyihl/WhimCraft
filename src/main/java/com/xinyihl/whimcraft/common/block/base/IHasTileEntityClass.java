package com.xinyihl.whimcraft.common.block.base;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntityClass {
    Class<? extends TileEntity> getTileEntityClass();
}
