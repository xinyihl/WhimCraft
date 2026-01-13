package com.xinyihl.whimcraft.api;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntityClass {
    Class<? extends TileEntity> getTileEntityClass();
}
