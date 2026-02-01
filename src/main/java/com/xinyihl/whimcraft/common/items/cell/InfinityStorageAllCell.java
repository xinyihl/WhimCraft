package com.xinyihl.whimcraft.common.items.cell;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.items.cell.base.InfinityStorageCellBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class InfinityStorageAllCell extends InfinityStorageCellBase {

    public InfinityStorageAllCell() {
        super();
        this.setRegistryName(Tags.MOD_ID, "infinity_storage_all_cell");
        this.setTranslationKey(Tags.MOD_ID + ".infinity_storage_all_cell");
    }

    @Override
    protected void addCheckedInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag advancedTooltips) {
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.1").getFormattedText());
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.2").getFormattedText());
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.3").getFormattedText());
        lines.add(new TextComponentTranslation("tooltip.whimcraft.infinity_storage_cell.4").getFormattedText());
        super.addCheckedInformation(stack, world, lines, advancedTooltips);
    }
}
