package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.Tags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;


public class Elgoog extends Item {

    public Elgoog() {
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID, "elgoog"));
        this.setTranslationKey(Tags.MOD_ID + ".elgoog");
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            if (((EntityPlayer) entityIn).getHeldItemMainhand().getItem() instanceof Elgoog) {
                entityIn.rotationYaw += 10;
                entityIn.rotationYaw %= 360.0f;
                if (entityIn.rotationYaw < 0.0f) {
                    entityIn.rotationYaw += 360.0f;
                }
                super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
            }
        }
    }

}
