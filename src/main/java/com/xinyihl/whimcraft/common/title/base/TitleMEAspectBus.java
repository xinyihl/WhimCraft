package com.xinyihl.whimcraft.common.title.base;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.me.GridAccessException;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;
import thaumicenergistics.integration.appeng.AEEssentiaStack;
import thaumicenergistics.integration.appeng.grid.GridUtil;
import thaumicenergistics.util.AEUtil;

public abstract class TitleMEAspectBus extends TitleMeBase implements MachineComponentTile {

    public TitleMEAspectBus() {
        this.proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    protected IEssentiaStorageChannel getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    public int addAspectToME(Aspect aspect, int i, boolean b) {
        EssentiaStack inContainer = new EssentiaStack(aspect, i);
        AEEssentiaStack toInsert = AEEssentiaStack.fromEssentiaStack(inContainer);
        try {
            IStorageGrid storage = GridUtil.getStorageGrid(this);
            IMEMonitor<IAEEssentiaStack> monitor = storage.getInventory(this.getChannel());
            if (monitor.canAccept(toInsert)) {
                IAEEssentiaStack notInserted = monitor.injectItems(toInsert, Actionable.SIMULATE, this.source);
                if (notInserted != null && notInserted.getStackSize() > 0) {
                    toInsert.decStackSize(notInserted.getStackSize());
                }
                if (b) {
                    monitor.injectItems(toInsert, Actionable.MODULATE, this.source);
                }
            }
            this.markDirty();
            return (int) toInsert.getStackSize();
        } catch (GridAccessException e) {
            //Ignore
        }
        return 0;
    }

    public int takeAspectFromME(Aspect aspect, int i, boolean b) {
        try {
            IStorageGrid storage = GridUtil.getStorageGrid(this);
            IMEMonitor<IAEEssentiaStack> monitor = storage.getInventory(this.getChannel());
            IAEEssentiaStack canExtract = monitor.extractItems(AEUtil.getAEStackFromAspect(aspect, i), Actionable.SIMULATE, this.source);

            if (canExtract == null) {
                return 0;
            }

            if (canExtract.getStackSize() != i) {
                return (int) canExtract.getStackSize();
            }

            if (b) {
                monitor.extractItems(canExtract, Actionable.MODULATE, this.source);
            }

            this.markDirty();
            return (int) canExtract.getStackSize();
        } catch (GridAccessException e) {
            //Ignore
        }
        return 0;
    }
}
