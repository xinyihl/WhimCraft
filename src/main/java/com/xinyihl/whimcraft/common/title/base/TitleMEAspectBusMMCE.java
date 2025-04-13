package com.xinyihl.whimcraft.common.title.base;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.me.helpers.MachineSource;
import appeng.util.Platform;
import com.xinyihl.whimcraft.common.api.IHasProbeInfo;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import kport.modularmagic.common.crafting.helper.AspectProviderCopy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.essentia.TileJarFillable;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;
import thaumicenergistics.integration.appeng.AEEssentiaStack;
import thaumicenergistics.integration.appeng.grid.GridUtil;
import thaumicenergistics.util.AEUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TitleMEAspectBusMMCE extends TileJarFillable implements MachineComponentTile, IActionHost, IGridProxyable, IHasProbeInfo {

    protected final AENetworkProxy proxy = new AENetworkProxy(this, "aeProxy", getVisualItemStack(), true);
    protected final IActionSource source;

    public TitleMEAspectBusMMCE() {
        this.source = new MachineSource(this);
        this.proxy.setIdlePowerUsage(100.0D);
        //this.amount = Integer.MAX_VALUE;
        //this.proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
    }

    public abstract ItemStack getVisualItemStack();

    protected void notifyNeighbors() {
        if (this.proxy.isActive()) {
            try {
                this.proxy.getTick().wakeDevice(this.proxy.getNode());
            } catch (GridAccessException e) {
                //Ignore
            }
            Platform.notifyBlocksOfNeighbors(this.getWorld(), this.getPos());
        }
    }

    @Override
    protected void setWorldCreate(@Nonnull final World worldIn) {
        setWorld(worldIn);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (!world.isRemote) {
            try {
                proxy.readFromNBT(tag);
            } catch (IllegalStateException e) {
                //Ignore
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        super.writeToNBT(tag);
        proxy.writeToNBT(tag);
        return tag;
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    public void sync() {
        if (!this.world.isRemote) {
            SPacketUpdateTileEntity packet = this.getUpdatePacket();
            PlayerChunkMapEntry trackingEntry = ((WorldServer) this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
            if (trackingEntry != null) {
                for (EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
                    player.connection.sendPacket(packet);
                }
            }
            this.notifyNeighbors();
            this.markDirty();
        }
    }

    @Nonnull
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 2555, this.getUpdateTag());
    }

    public final void onDataPacket(@Nonnull NetworkManager manager, @Nonnull SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Nonnull
    @Override
    public IGridNode getActionableNode() {
        return proxy.getNode();
    }

    @Override
    public AENetworkProxy getProxy() {
        return proxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public void gridChanged() {

    }

    @Nullable
    @Override
    public IGridNode getGridNode(@Nonnull AEPartLocation aePartLocation) {
        return proxy.getNode();
    }

    @Nonnull
    @Override
    public AECableType getCableConnectionType(@Nonnull AEPartLocation aePartLocation) {
        return AECableType.SMART;
    }

    @Override
    public void securityBreak() {
        getWorld().destroyBlock(getPos(), true);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        proxy.onChunkUnload();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        proxy.invalidate();
    }

    @Override
    public void validate() {
        super.validate();
        proxy.validate();
        proxy.onReady();
    }

    public void setOwner(EntityPlayer placer) {
        proxy.setOwner(placer);
    }

    public void addProbeInfo(Consumer<String> consumer, Function<String, String> loc) {
        if (this.proxy.isPowered()) {
            if (this.proxy.isActive()) {
                consumer.accept(loc.apply("tile_me_base.online"));
            } else {
                consumer.accept(loc.apply("tile_me_base.missing_channel"));
            }
        } else {
            consumer.accept(loc.apply("tile_me_base.offline"));
        }
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
                this.markDirty();
                if (notInserted != null && notInserted.getStackSize() > 0) {
                    return (int) notInserted.getStackSize();
                }
            }
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

    @Override
    public boolean canInputFrom(EnumFacing face) {
        return false;
    }

    @Override
    public boolean canOutputTo(EnumFacing face) {
        return false;
    }

    @Override
    public boolean isConnectable(EnumFacing face) {
        return false;
    }

    @Deprecated
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Deprecated
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public void update() {
    }

    public boolean isBlocked() {
        return false;
    }

    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    public int containerContains(Aspect aspect) {
        return 0;
    }

    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    public void setAspects(AspectList aspectList) {
    }

    @Override
    @Optional.Method(modid = "thaumcraft")
    public synchronized int addToContainer(final Aspect tt, final int am) {
        return addAspectToME(tt, am, true);
        //return super.addToContainer(tt, am);
    }

    @Override
    @Optional.Method(modid = "thaumcraft")
    public synchronized boolean takeFromContainer(final Aspect tt, final int am) {
        return takeAspectFromME(tt, am, true) == am;
        //return super.takeFromContainer(tt, am);
    }

    public static class MYAspectProviderCopy extends AspectProviderCopy {
        public MYAspectProviderCopy(TileJarFillable jar) {
            super(jar);
        }

        @Override
        public int addToContainer(final Aspect tt, final int am) {
            return ((TitleMEAspectBusMMCE) this.getOriginal()).addAspectToME(tt, am, false);
        }

        @Override
        public boolean takeFromContainer(final Aspect tt, final int am) {
            return ((TitleMEAspectBusMMCE) this.getOriginal()).takeAspectFromME(tt, am, false) == am;
        }
    }

}
