package com.xinyihl.whimcraft.common.mixins.nae2;

import co.neeve.nae2.common.items.cells.DenseCell;
import co.neeve.nae2.common.registration.definitions.Materials;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DenseCell.class, remap = false)
public abstract class DenseCellMixin {
    @Final
    @Shadow
    protected Materials.MaterialType component;

    /**
     * @author xinyihl
     * @reason 修改存储元件存储种类
     */
    @Overwrite
    public int getTotalTypes(ItemStack cellItem) {
        switch (component) {
            case CELL_PART_256K:
                return Configurations.AEMOD_CONFIG.AECellConfig.cell256K;
            case CELL_PART_1024K:
                return Configurations.AEMOD_CONFIG.AECellConfig.cell1024K;
            case CELL_PART_4096K:
                return Configurations.AEMOD_CONFIG.AECellConfig.cell4096K;
            case CELL_PART_16384K:
                return Configurations.AEMOD_CONFIG.AECellConfig.cell16384K;
            default:
                return 0;
        }
    }

    /**
     * @author xinyihl
     * @reason 修改一种物品占用字节
     */
    @Overwrite
    public int getBytesPerType(ItemStack itemStack) {
        switch (component) {
            case CELL_PART_256K:
            case CELL_FLUID_PART_256K:
                return 1024;
            case CELL_PART_1024K:
            case CELL_FLUID_PART_1024K:
                return 2048;
            case CELL_PART_4096K:
            case CELL_FLUID_PART_4096K:
                return 4096;
            case CELL_PART_16384K:
            case CELL_FLUID_PART_16384K:
                return 8192;
            default:
                return 0;
        }
    }
}
