package com.xinyihl.whimcraft.common.items.cell;

import appeng.api.storage.data.IAEItemStack;
import appeng.items.AEBaseItem;
import appeng.util.item.AEItemStack;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class InfinityListItemCell extends AEBaseItem {
    public InfinityListItemCell() {
        this.setMaxStackSize(1);
        this.setRegistryName(Tags.MOD_ID, "infinity_list_item_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_list_item_cell");
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static List<IAEItemStack> getRecords(ItemStack stack) {
        List<IAEItemStack> records = new ArrayList<>();
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("recs", Constants.NBT.TAG_LIST)) {
                NBTTagList list = tag.getTagList("recs", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++) {
                    records.add(AEItemStack.fromNBT(list.getCompoundTagAt(i)));
                }
            }
        }
        return records;
    }

    public static ItemStack createWithRecords(List<ItemStack> records) {
        if (IB.itemInfinityListItemCell == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(IB.itemInfinityListItemCell);
        setRecords(stack, records);
        return stack;
    }

    public static void setRecords(ItemStack stack, List<ItemStack> records) {
        if (stack.isEmpty()) {
            return;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagList list = new NBTTagList();
        for (ItemStack record : records) {
            if (record == null || record.isEmpty()) {
                continue;
            }
            IAEItemStack aeItemStack = AEItemStack.fromItemStack(record);
            if (aeItemStack != null) {
                NBTTagCompound recTag = new NBTTagCompound();
                aeItemStack.writeToNBT(recTag);
                list.appendTag(recTag);
            }
        }
        if (list.tagCount() > 0) {
            tag.setTag("recs", list);
            stack.setTagCompound(tag);
        }
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        List<IAEItemStack> records = getRecords(stack);
        int maxShow = 5;
        if (!records.isEmpty()) {
            int count = Math.min(records.size(), maxShow);
            for (int i = 0; i < count; i++) {
                IAEItemStack record = records.get(i);
                if (record == null) continue;
                lines.add(record.getDefinition().getDisplayName());
            }
            if (records.size() > maxShow) {
                lines.add("...");
            }
        }
    }
}
