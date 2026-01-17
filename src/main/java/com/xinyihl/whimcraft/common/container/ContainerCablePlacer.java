package com.xinyihl.whimcraft.common.container;

import com.xinyihl.whimcraft.api.CableCompatManager;
import com.xinyihl.whimcraft.common.items.placer.CablePlacer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerCablePlacer extends Container {

    private final EntityPlayer player;

    public ContainerCablePlacer(EntityPlayer player) {
        this.player = player;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        if (slotId >= 0 && slotId < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack clickedStack = slot.getStack();
            ItemStack tool = player.getHeldItemMainhand();
            if (!clickedStack.isEmpty() && tool.getItem() instanceof CablePlacer && clickedStack.getItem() != tool.getItem()) {
                if (CableCompatManager.canSelect(clickedStack)) {
                    // 立刻更新（server 负责同步给 client；client 侧也会马上读到本地 NBT）
                    ItemStack selected = clickedStack.copy();
                    selected.setCount(1);
                    CablePlacer.setCableStack(tool, selected);
                    if (!player.world.isRemote) {
                        // 确保手持物品 NBT 变更立即同步到客户端，避免需要重新打开 GUI 才看到。
                        this.detectAndSendChanges();
                    }
                    return clickedStack;
                } else if (!player.world.isRemote) {
                    player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.unsupported"), true);
                }
            }
        }
        return super.slotClick(slotId, dragType, clickType, player);
    }

    public void onAction(String type, NBTTagCompound compound) {
        ItemStack tool = player.getHeldItemMainhand();
        if (!(tool.getItem() instanceof CablePlacer)) {
            return;
        }

        switch (type) {
            case "set_cable": {
                NBTTagCompound nbt = compound.getCompoundTag("nbt");
                ItemStack clickedStack = new ItemStack(nbt);
                if (!clickedStack.isEmpty()) {
                    if (CableCompatManager.canSelect(clickedStack)) {
                        CablePlacer.setCableStack(tool, clickedStack);
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.unsupported"), true);
                    }
                }
                break;
            }
            case "toggle_allow_replace": {
                boolean value = compound.getBoolean("value");
                CablePlacer.setOptAllowReplace(tool, value);
                break;
            }
            case "clear_points": {
                CablePlacer.clearAll(tool);
                break;
            }
        }
    }
}
