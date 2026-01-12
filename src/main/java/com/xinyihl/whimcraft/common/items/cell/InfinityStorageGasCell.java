package com.xinyihl.whimcraft.common.items.cell;

import appeng.items.AEBaseItem;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class InfinityStorageGasCell extends AEBaseItem {
    public static final String NBT_UUID = "wc_uuid";
    public static final String NBT_GAS_TYPES = "wc_gas_types";
    public static final String NBT_DATA_BYTES = "wc_data_bytes";

    public InfinityStorageGasCell() {
        this.setMaxStackSize(1);
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_gas_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_gas_cell");
        this.setCreativeTab(IB.CREATIVE_TAB);
    }

    public static UUID getOrCreateUuid(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        if (tag.hasUniqueId(NBT_UUID)) {
            try {
                return tag.getUniqueId(NBT_UUID);
            } catch (Exception ignored) {
                // fallthrough
            }
        }

        UUID uuid = UUID.randomUUID();
        tag.setUniqueId(NBT_UUID, uuid);
        if (!tag.hasKey(NBT_GAS_TYPES)) tag.setInteger(NBT_GAS_TYPES, 0);
        if (!tag.hasKey(NBT_DATA_BYTES)) tag.setLong(NBT_DATA_BYTES, 0L);
        return uuid;
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        NBTTagCompound tag = stack.getTagCompound();
        int gasTypes = tag != null ? tag.getInteger(NBT_GAS_TYPES) : 0;
        long bytes = tag != null ? tag.getLong(NBT_DATA_BYTES) : 0L;
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_gas_cell.stats", gasTypes, bytes)
                .setStyle(new Style().setColor(TextFormatting.GRAY))
                .getFormattedText());
        if (tag != null && tag.hasUniqueId(NBT_UUID)) {
            lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.uuid", tag.getUniqueId(NBT_UUID).toString()).getFormattedText());
        }
    }
}
