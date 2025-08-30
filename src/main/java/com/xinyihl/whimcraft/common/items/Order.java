package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.Tags;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class Order extends Item {

    public Order(){
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID,"order"));
        this.setTranslationKey(Tags.MOD_ID + ".order");
    }

}
