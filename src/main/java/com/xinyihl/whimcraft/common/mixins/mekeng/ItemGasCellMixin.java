package com.xinyihl.whimcraft.common.mixins.mekeng;

import com.mekeng.github.common.item.ItemGasCell;
import com.xinyihl.whimcraft.Configurations;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.mekeng.github.common.ItemAndBlocks.*;

@Mixin(value = ItemGasCell.class, remap = false)
public abstract class ItemGasCellMixin {
    @Final
    @Shadow
    private ItemStack core;

    /**
     * @author xinyihl
     * @reason 修改存储元件存储种类
     */
    @Overwrite
    public int getTotalTypes(ItemStack cellItem) {
        Item item = core.getItem();
        if (GAS_CELL_CORE_1k == item) return Configurations.AEMOD_CONFIG.AECellConfig.gasCell1K;
        if (GAS_CELL_CORE_4k == item) return Configurations.AEMOD_CONFIG.AECellConfig.gasCell4K;
        if (GAS_CELL_CORE_16k == item) return Configurations.AEMOD_CONFIG.AECellConfig.gasCell16K;
        if (GAS_CELL_CORE_64k == item) return Configurations.AEMOD_CONFIG.AECellConfig.gasCell64K;
        return 0;
    }
}
