package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.fluids.items.BasicFluidStorageCell;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BasicFluidStorageCell.class, remap = false)
public abstract class BasicFluidStorageCellMixin extends AbstractStorageCellMixin {
    /**
     * @author xinyihl
     * @reason 修改存储元件存储种类
     */
    @Overwrite
    public int getTotalTypes(ItemStack cellItem) {
        switch (component) {
            case FLUID_CELL1K_PART:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell1K;
            case FLUID_CELL4K_PART:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell4K;
            case FLUID_CELL16K_PART:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell16K;
            case FLUID_CELL64K_PART:
                return Configurations.AEMOD_CONFIG.AECellConfig.fluidCell64K;
            default:
                return 0;
        }
    }
}
