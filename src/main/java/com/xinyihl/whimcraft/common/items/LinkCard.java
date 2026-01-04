package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.Mods;
import com.xinyihl.whimcraft.common.tile.TileShareInfHandler;
import com.xinyihl.whimcraft.common.tile.base.TileRedisInterfaceBase;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class LinkCard extends Item {

    private static final String NBT_POS = "link_card_pos";
    private static final String NBT_REDIS_UUID = "link_card_redis_uuid";

    public LinkCard() {
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID, "link_card"));
        this.setTranslationKey(Tags.MOD_ID + ".link_card");
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return EnumActionResult.SUCCESS;
        ItemStack itemStack = player.getHeldItem(hand);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (handleRedisInterfaceLinking(player, itemStack, tileEntity)) {
            return EnumActionResult.SUCCESS;
        }

        if (Mods.MMCE.isLoaded() && Configurations.MMCE_CONFIG.useShareInfHandler && handleShareInfHandlerLinking(player, pos, itemStack, tileEntity)) {
            return EnumActionResult.SUCCESS;
        }

        if (player.isSneaking()) {
            itemStack.setTagCompound(null);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.clear"), true);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    private boolean handleRedisInterfaceLinking(EntityPlayer player, ItemStack itemStack, TileEntity tileEntity) {
        if (tileEntity instanceof TileRedisInterfaceBase) {
            TileRedisInterfaceBase redisTile = (TileRedisInterfaceBase) tileEntity;
            if (player.isSneaking()) {
                UUID uuid = redisTile.ensureUuid();
                NBTTagCompound tag = itemStack.getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setUniqueId(NBT_REDIS_UUID, uuid);
                itemStack.setTagCompound(tag);
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.redis.save"), true);
            } else {
                NBTTagCompound tag = itemStack.getTagCompound();
                if (tag == null || !tag.hasUniqueId(NBT_REDIS_UUID)) {
                    player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.redis.error.noUuid"), true);
                } else {
                    UUID uuid = tag.getUniqueId(NBT_REDIS_UUID);
                    redisTile.setUuid(uuid);
                    player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.redis.bind"), true);
                }
            }
            return true;
        }
        return false;
    }

    @Optional.Method(modid = "modularmachinery")
    private boolean handleShareInfHandlerLinking(EntityPlayer player, BlockPos pos, ItemStack itemStack, TileEntity tileEntity) {
        if (tileEntity instanceof MEPatternProvider) {
            if (player.isSneaking()) {
                NBTTagCompound tag = itemStack.getTagCompound();
                if (tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setLong(NBT_POS, pos.toLong());
                itemStack.setTagCompound(tag);
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.save"), true);
            }
            return true;
        }
        if (tileEntity instanceof TileShareInfHandler) {
            NBTTagCompound tag = itemStack.getTagCompound();
            if (tag == null || !tag.hasKey(NBT_POS)) {
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.linkgard.error.noPos"), true);
            } else {
                BlockPos blockPos = BlockPos.fromLong(tag.getLong(NBT_POS));
                ((TileShareInfHandler) tileEntity).setBlockPos(player, blockPos);
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            if (tag.hasUniqueId(NBT_REDIS_UUID)) {
                tooltip.add("UUID: " + tag.getUniqueId(NBT_REDIS_UUID).toString());
            }
            if (tag.hasKey(NBT_POS)) {
                BlockPos blockPos = BlockPos.fromLong(tag.getLong(NBT_POS));
                tooltip.add("Pos: " + blockPos.getX() + "/" + blockPos.getY() + "/" + blockPos.getZ());
            }
        } else {
            tooltip.add(I18n.format("tooltip.whimcraft.linkgard.no"));
        }
    }
}
