package com.xinyihl.whimcraft.common.items.cell;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.fluids.util.AEFluidStack;
import appeng.items.AEBaseItem;
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

public class InfinityListFluidCell extends AEBaseItem {
    public InfinityListFluidCell() {
        this.setMaxStackSize(1);
        this.setRegistryName(Tags.MOD_ID, "infinity_list_fluid_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_list_fluid_cell");
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static List<Object> getRecords(ItemStack stack) {
        List<Object> records = new ArrayList<>();
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("recs", Constants.NBT.TAG_LIST)) {
                NBTTagList list = tag.getTagList("recs", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++) {
                    records.add(AEFluidStack.fromNBT(list.getCompoundTagAt(i)));
                }
            }
        }
        return records;
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        List<Object> records = getRecords(stack);
        if (!records.isEmpty()) {
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
