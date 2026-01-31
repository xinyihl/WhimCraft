package com.xinyihl.whimcraft.common.mixins.appliedenergistics2;

import appeng.api.AEApi;
import appeng.api.storage.ICellHandler;
import appeng.api.storage.ICellInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.me.storage.DriveWatcher;
import appeng.tile.grid.AENetworkInvTile;
import appeng.tile.inventory.AppEngCellInventory;
import appeng.tile.storage.TileDrive;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(value = TileDrive.class, remap = false)
public abstract class TileDriveMixin extends AENetworkInvTile {

    @Shadow
    private boolean isCached;

    @Shadow
    private int priority;

    @Final
    @Shadow
    private AppEngCellInventory inv;

    @Final
    @Shadow
    private Map<IStorageChannel<? extends IAEStack<?>>, List> inventoryHandlers;

    @Final
    @Shadow
    private DriveWatcher<IAEItemStack>[] invBySlot;

    @Final
    @Shadow
    private ICellHandler[] handlersBySlot;

    @Inject(method = "updateState", at = @At("HEAD"), cancellable = true)
    private void updateStateMultiChannel(CallbackInfo ci) {
        if (this.isCached) {
            ci.cancel();
            return;
        }

        Collection<IStorageChannel<? extends IAEStack<?>>> storageChannels = AEApi.instance().storage().storageChannels();
        storageChannels.forEach(channel -> this.inventoryHandlers.put(channel, new ArrayList<>(10)));
        double power = 2.0;

        for (int x = 0; x < inv.getSlots(); x++) {
            ItemStack itemStack = inv.getStackInSlot(x);
            this.invBySlot[x] = null;
            this.handlersBySlot[x] = null;

            if (itemStack.isEmpty()) {
                continue;
            }

            ICellHandler handler = AEApi.instance().registries().cell().getHandler(itemStack);
            this.handlersBySlot[x] = handler;

            if (handler == null) {
                continue;
            }

            for (IStorageChannel<? extends IAEStack<?>> channel : storageChannels) {
                ICellInventoryHandler<? extends IAEStack<?>> cell = handler.getCellInventory(itemStack, (TileDrive) (Object) this, channel);
                if (cell != null) {
                    inv.setHandler(x, cell);
                    power += handler.cellIdleDrain(itemStack, cell);
                    DriveWatcher<IAEItemStack> driveWatcher = new DriveWatcher(cell, itemStack, handler, (TileDrive) (Object) this);
                    driveWatcher.setPriority(this.priority);
                    if (this.invBySlot[x] == null) {
                        this.invBySlot[x] = driveWatcher;
                    }
                    this.inventoryHandlers.get(channel).add(driveWatcher);
                }
            }
        }

        this.getProxy().setIdlePowerUsage(power);
        this.isCached = true;
        ci.cancel();
    }
}
