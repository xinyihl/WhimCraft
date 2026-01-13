package com.xinyihl.whimcraft.common.items.cell;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;

public class InfinityStorageFluidCell extends InfinityStorageCellBase {

    public InfinityStorageFluidCell() {
        super();
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_fluid_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_fluid_cell");
    }
}
