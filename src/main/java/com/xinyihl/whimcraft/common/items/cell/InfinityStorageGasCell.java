package com.xinyihl.whimcraft.common.items.cell;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;

public class InfinityStorageGasCell extends InfinityStorageCellBase {

    public InfinityStorageGasCell() {
        super();
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_gas_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_gas_cell");
    }
}
