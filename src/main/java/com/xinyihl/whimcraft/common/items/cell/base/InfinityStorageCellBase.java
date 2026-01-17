package com.xinyihl.whimcraft.common.items.cell.base;

import appeng.items.AEBaseItem;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.utils.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public abstract class InfinityStorageCellBase extends AEBaseItem {
    public static final String NBT_UUID = "wc_uuid";
    public static final String NBT_TYPES = "wc_types";
    public static final String NBT_BYTES = "wc_bytes";

    protected InfinityStorageCellBase() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static UUID getOrCreateUuid(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        if (tag.hasUniqueId(NBT_UUID)) {
            return tag.getUniqueId(NBT_UUID);
        }

        UUID uuid = UUID.randomUUID();
        tag.setUniqueId(NBT_UUID, uuid);
        if (!tag.hasKey(NBT_TYPES)) tag.setInteger(NBT_TYPES, 0);
        if (!tag.hasKey(NBT_BYTES)) tag.setString(NBT_BYTES, "0");
        return uuid;
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        NBTTagCompound tag = stack.getTagCompound();
        int fluidTypes = tag != null ? tag.getInteger(NBT_TYPES) : 0;
        String bytesStr = tag != null ? tag.getString(NBT_BYTES) : "0";
        String formattedBytes = Utils.formatBytes(bytesStr);
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.stats", fluidTypes, formattedBytes).setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
        if (tag != null && tag.hasUniqueId(NBT_UUID)) {
            lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.uuid", tag.getUniqueId(NBT_UUID).toString()).getFormattedText());
        }
    }
}
