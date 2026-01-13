package com.xinyihl.whimcraft.common.items.cell;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;

public class InfinityStorageItemCell extends InfinityStorageCellBase {

    public InfinityStorageItemCell() {
        super();
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_item_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_item_cell");
    }
}
