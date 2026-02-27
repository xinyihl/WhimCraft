package com.xinyihl.whimcraft.common.items.cell;

import appeng.items.AEBaseItem;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.integration.appeng.AEEssentiaStack;

import java.util.ArrayList;
import java.util.List;

public class InfinityListEssentiaCell extends AEBaseItem {

    public InfinityListEssentiaCell() {
        this.setMaxStackSize(1);
        this.setRegistryName(Tags.MOD_ID, "infinity_list_essentia_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_list_essentia_cell");
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static List<IAEEssentiaStack> getRecords(ItemStack stack) {
        List<IAEEssentiaStack> records = new ArrayList<>();
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("recs", Constants.NBT.TAG_LIST)) {
                NBTTagList list = tag.getTagList("recs", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++) {
                    records.add(AEEssentiaStack.fromNBT(list.getCompoundTagAt(i)));
                }
            }
        }
        return records;
    }

    public static ItemStack createWithRecords(List<Aspect> records) {
        if (IB.itemInfinityListEssentiaCell == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = new ItemStack(IB.itemInfinityListEssentiaCell);
        setRecords(stack, records);
        return stack;
    }

    public static void setRecords(ItemStack stack, List<Aspect> records) {
        if (stack.isEmpty()) {
            return;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        NBTTagList list = new NBTTagList();
        for (Aspect record : records) {
            if (record == null) {
                continue;
            }
            IAEEssentiaStack aeStack = AEEssentiaStack.fromEssentiaStack(new EssentiaStack(record, 1));
            if (aeStack != null) {
                NBTTagCompound recTag = new NBTTagCompound();
                aeStack.writeToNBT(recTag);
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
        List<IAEEssentiaStack> records = getRecords(stack);
        int maxShow = 5;
        if (!records.isEmpty()) {
            int count = Math.min(records.size(), maxShow);
            for (int i = 0; i < count; i++) {
                IAEEssentiaStack record = records.get(i);
                if (record == null) continue;
                Aspect aspect = record.getAspect();
                if (aspect != null) {
                    lines.add(aspect.getName());
                }
            }
            if (records.size() > maxShow) {
                lines.add("...");
            }
        }
    }
}
