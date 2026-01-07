package com.xinyihl.whimcraft.common.api;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntityClass {
    Class<? extends TileEntity> getTileEntityClass();
}
