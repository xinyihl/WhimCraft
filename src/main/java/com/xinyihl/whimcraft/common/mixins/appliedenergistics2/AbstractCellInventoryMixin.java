package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.me.storage.AbstractCellInventory;
import com.xinyihl.whimcraft.Configurations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = AbstractCellInventory.class, remap = false)
public abstract class AbstractCellInventoryMixin {
    @Unique
    private static final String[] ITEM_SLOT_KEYS = new String[Configurations.AEMOD_CONFIG.aeTotalTypes];
    @Unique
    private static final String[] ITEM_SLOT_COUNT_KEYS = new String[Configurations.AEMOD_CONFIG.aeTotalTypes];
    @Unique
    private static final int MAX_ITEM_TYPES = Configurations.AEMOD_CONFIG.aeTotalTypes;

    /**
     * @author xinyihl
     * @reason 修改存储元件存储种类上限
     */
    @ModifyConstant(
            method = {
                    "<init>",
                    "<clinit>"
            },
            constant = @Constant(intValue = 63)
    )
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private static int injected(int original) {
        return Configurations.AEMOD_CONFIG.aeTotalTypes;
    }
}
