package com.xinyihl.whimcraft.common.tile.base;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.me.GridAccessException;
import github.kasuminova.mmce.common.util.Sides;
import hellfirepvp.modularmachinery.client.ClientProxy;
import hellfirepvp.modularmachinery.common.data.Config;
import hellfirepvp.modularmachinery.common.tiles.base.ColorableMachineTile;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;
import thaumicenergistics.integration.appeng.AEEssentiaStack;
import thaumicenergistics.integration.appeng.grid.GridUtil;
import thaumicenergistics.util.AEUtil;

import javax.annotation.Nonnull;

public abstract class TileMEAspectBus extends TileMeBase implements MachineComponentTile, ColorableMachineTile {

    private int definedColor;

    public TileMEAspectBus() {
        this.definedColor = Config.machineColor;
        this.proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    public int getMachineColor() {
        return this.definedColor;
    }

    public void setMachineColor(int newColor) {
        if (this.definedColor != newColor) {
            this.definedColor = newColor;
            this.sync();
        }
    }

    protected IEssentiaStorageChannel getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (!tag.hasKey("casingColor")) {
            this.definedColor = Config.machineColor;
        } else {
            int newColor = tag.getInteger("casingColor");
            if (this.definedColor != newColor) {
                this.definedColor = newColor;
                Sides.CLIENT.runIfPresent(() -> ClientProxy.clientScheduler.addRunnable(() -> {
                    World world = this.getWorld();
                    world.addBlockEvent(this.pos, world.getBlockState(this.pos).getBlock(), 1, 1);
                }, 0));
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("casingColor", this.definedColor);
        return tag;
    }

    public synchronized int addAspectToME(Aspect aspect, int i, boolean b) {
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

    public synchronized int takeAspectFromME(Aspect aspect, int i, boolean b) {
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
