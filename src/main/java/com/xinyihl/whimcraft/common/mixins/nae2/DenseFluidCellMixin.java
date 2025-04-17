package com.xinyihl.whimcraft.common.mixins.nae2;

import co.neeve.nae2.common.items.cells.DenseFluidCell;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = DenseFluidCell.class, remap = false)
public abstract class DenseFluidCellMixin extends DenseCellMixin {
    /**
     * @author xinyihl
     * @reason 修改存储元件存储种类
     */
    @Overwrite
    public int getTotalTypes(ItemStack cellItem) {
        switch (component) {
            case CELL_FLUID_PART_256K:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell256K;
            case CELL_FLUID_PART_1024K:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell1024K;
            case CELL_FLUID_PART_4096K:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell4096K;
            case CELL_FLUID_PART_16384K:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell16384K;
            default:
                return 0;
        }
    }
}
