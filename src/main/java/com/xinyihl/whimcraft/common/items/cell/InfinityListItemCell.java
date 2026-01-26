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
