package com.xinyihl.whimcraft.common.tile;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.me.GridAccessException;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.init.IB;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileMEPullBus extends MEMachineComponent implements IGridTickable {

    private static final int SLOT_COUNT = 16;

    private final IItemStorageChannel channel = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);

    private IOInventory inventory = buildInventory();

    private IOInventory buildInventory() {
        int[] slotIDs = new int[SLOT_COUNT];
        for (int i = 0; i < SLOT_COUNT; i++) {
            slotIDs[i] = i;
        }
        return new IOInventory(this, slotIDs, new int[]{});
    }

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(IB.itemMEPullBus);
    }

    @Nullable
    @Override
    public MachineComponent<?> provideComponent() {
        return new MachineComponent.ItemBus(IOType.INPUT) {
            @Override
            public IOInventory getContainerProvider() {
                return inventory;
            }
        };
    }

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull IGridNode node) {
        int interval = Math.max(1, Configurations.AEMOD_CONFIG.mePullBusInterval);
        return new TickingRequest(interval, interval, !hasEmptySlot(), false);
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull IGridNode node, int ticksSinceLastCall) {
        if (!proxy.isActive()) {
            return TickRateModulation.IDLE;
        }

        try {
            IMEMonitor<IAEItemStack> inv = proxy.getStorage().getInventory(channel);
            IItemList<IAEItemStack> storageList = inv.getStorageList();

            // Copy to list to avoid ConcurrentModificationException
            List<IAEItemStack> snapshot = new ArrayList<>();
            for (IAEItemStack s : storageList) {
                snapshot.add(s.copy());
            }

            boolean extracted = false;

            // First, keep pulling already tracked item types in inventory.
            for (int slot = 0; slot < SLOT_COUNT; slot++) {
                ItemStack existing = inventory.getStackInSlot(slot);
                if (existing.isEmpty()) {
                    continue;
                }

                int spaceLeft = Integer.MAX_VALUE - existing.getCount();
                if (spaceLeft <= 0) {
                    continue;
                }

                IAEItemStack toExtract = AEItemStack.fromItemStack(existing.copy());
                if (toExtract == null) {
                    continue;
                }
                toExtract.setStackSize(spaceLeft);

                IAEItemStack result = Platform.poweredExtraction(proxy.getEnergy(), inv, toExtract, source);
                if (result != null && result.getStackSize() > 0) {
                    existing.grow((int) result.getStackSize());
                    inventory.setStackInSlot(slot, existing);
                    extracted = true;
                }
            }

            // Then, if inventory has empty slots, randomly pull new item types until 16 are tracked.
            for (IAEItemStack aeStack : snapshot) {
                if (aeStack.getStackSize() <= 0) {
                    continue;
                }

                ItemStack aeItemStack = aeStack.createItemStack();
                if (aeItemStack.isEmpty()) {
                    continue;
                }

                boolean alreadyTracked = false;
                int emptySlot = -1;
                for (int i = 0; i < SLOT_COUNT; i++) {
                    ItemStack existing = inventory.getStackInSlot(i);
                    if (existing.isEmpty()) {
                        if (emptySlot == -1) {
                            emptySlot = i;
                        }
                    } else if (ItemStack.areItemsEqual(existing, aeItemStack) && ItemStack.areItemStackTagsEqual(existing, aeItemStack)) {
                        alreadyTracked = true;
                        break;
                    }
                }

                if (alreadyTracked) {
                    continue;
                }
                if (emptySlot == -1) {
                    break;
                }

                // Pull a new random item type into the first empty slot.
                IAEItemStack toExtract = aeStack.copy();
                toExtract.setStackSize(Integer.MAX_VALUE);

                IAEItemStack result = Platform.poweredExtraction(proxy.getEnergy(), inv, toExtract, source);
                if (result != null && result.getStackSize() > 0) {
                    inventory.setStackInSlot(emptySlot, result.createItemStack());
                    extracted = true;
                }
            }

            return extracted ? TickRateModulation.FASTER : TickRateModulation.SLOWER;
        } catch (GridAccessException e) {
            return TickRateModulation.IDLE;
        }
    }

    private boolean hasEmptySlot() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        if (compound.hasKey("inventory")) {
            inventory = IOInventory.deserialize(this, compound.getCompoundTag("inventory"));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setTag("inventory", inventory.writeNBT());
    }

    public void returnItemsToAE() {
        if (world == null || world.isRemote || !proxy.isActive()) {
            return;
        }
        try {
            IMEMonitor<IAEItemStack> monitor = proxy.getStorage().getInventory(channel);
            for (int i = 0; i < SLOT_COUNT; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                IAEItemStack toInsert = AEItemStack.fromItemStack(stack);
                if (toInsert == null) {
                    continue;
                }
                IAEItemStack remaining = Platform.poweredInsert(proxy.getEnergy(), monitor, toInsert, source);
                if (remaining == null || remaining.getStackSize() == 0) {
                    inventory.setStackInSlot(i, ItemStack.EMPTY);
                } else {
                    inventory.setStackInSlot(i, remaining.createItemStack());
                }
            }
        } catch (GridAccessException ignored) {
        }
    }

    public IOInventory getInternalInventory() {
        return inventory;
    }

    public void setInventory(IOInventory inventory) {
        this.inventory = inventory;
    }
}
