package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.core.features.registries.cell.CreativeCellHandler;
import appeng.me.storage.DriveWatcher;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.xinyihl.whimcraft.common.api.IInfinityListCellHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(value = DriveWatcher.class, remap = false)
public abstract class DriveWatcherMixin {
    @WrapOperation(
            method = {"injectItems", "extractItems"},
            constant = {@Constant(classValue = CreativeCellHandler.class)}
    )
    private static boolean wrapInstanceOfCheck(Object obj, Operation<Boolean> operation) {
        return operation.call(obj) || obj instanceof IInfinityListCellHandler;
    }
}
