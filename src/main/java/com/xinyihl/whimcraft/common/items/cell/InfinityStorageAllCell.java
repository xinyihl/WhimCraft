package com.xinyihl.whimcraft.common.items.cell;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;

public class InfinityStorageAllCell extends InfinityStorageCellBase {

    public InfinityStorageAllCell() {
        super();
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_all_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_all_cell");
    }
}
