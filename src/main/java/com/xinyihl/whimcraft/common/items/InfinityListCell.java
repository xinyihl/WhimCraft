package com.xinyihl.whimcraft.common.items;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.fluids.util.AEFluidStack;
import appeng.items.AEBaseItem;
import appeng.util.item.AEItemStack;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class InfinityListCell extends AEBaseItem implements ICellWorkbenchItem {
    public InfinityListCell() {
        this.setMaxStackSize(1);
        this.setRegistryName(Tags.MOD_ID, "infinity_list_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_list_cell");
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static List<Object> getRecords(ItemStack stack) {
        List<Object> records = new ArrayList<>();
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("recs", Constants.NBT.TAG_LIST)) {
                NBTTagList list = tag.getTagList("recs", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound entry = list.getCompoundTagAt(i);
                    String type = entry.getString("t");
                    if ("f".equals(type)) {
                        records.add(AEFluidStack.fromNBT(entry.getCompoundTag("r")));
                    } else if ("i".equals(type)) {
                        records.add(AEItemStack.fromNBT(entry.getCompoundTag("r")));
                    }
                }
            }
        }
        return records;
    }

    @Override
    public boolean isEditable(ItemStack itemStack) {
        return false;
    }

    @Override
    public IItemHandler getUpgradesInventory(ItemStack itemStack) {
        return null;
    }

    @Override
    public IItemHandler getConfigInventory(ItemStack itemStack) {
        return null;
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return FuzzyMode.IGNORE_ALL;
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {

    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        List<Object> records = getRecords(stack);
        if (records.isEmpty()) {
            lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_list_cell").getFormattedText());
        } else {
            for (Object obj : records) {
                if (obj instanceof IAEItemStack) {
                    lines.add(((IAEItemStack) obj).getDefinition().getDisplayName());
                } else if (obj instanceof IAEFluidStack) {
                    lines.add(((IAEFluidStack) obj).getFluidStack().getLocalizedName());
                }
            }
        }
    }
}
