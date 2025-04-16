package com.xinyihl.whimcraft.common.items;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.common.init.IB;
import com.xinyihl.whimcraft.common.title.TitleShareInfHandler;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class LinkCard extends Item {

    public LinkCard(){
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID,"link_card"));
        this.setTranslationKey(Tags.MOD_ID + ".link_card");
        IB.items.add(this);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) return EnumActionResult.PASS;
        ItemStack itemStack = player.getHeldItem(hand);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(player.isSneaking()){
            if (!(tileEntity instanceof MEPatternProvider)){
                itemStack.setTagCompound(null);
                player.sendStatusMessage(new TextComponentString("§a清除坐标成功！"), true);
            } else {
                NBTTagCompound nbtpos = new NBTTagCompound();
                nbtpos.setLong("link_card_pos", pos.toLong());
                itemStack.setTagCompound(nbtpos);
                player.sendStatusMessage(new TextComponentString("§a保存坐标成功！"), true);
            }
        }else {
            if (!(tileEntity instanceof TitleShareInfHandler)){
                player.sendStatusMessage(new TextComponentString("§c设置失败，目标不是库存共享总线！"), true);
            } else {
                NBTTagCompound nbtpos = itemStack.getTagCompound();
                if (nbtpos == null) {
                    player.sendStatusMessage(new TextComponentString("§c设置失败，未保存坐标！"), true);
                } else {
                    BlockPos blockPos = BlockPos.fromLong(nbtpos.getLong("link_card_pos"));
                    ((TitleShareInfHandler) tileEntity).setBlockPos(player, blockPos);
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn,@Nonnull List<String> tooltip,@Nonnull ITooltipFlag flagIn)
    {
        NBTTagCompound nbtpos = stack.getTagCompound();
        if (nbtpos != null) {
            //NBTTagCompound nbt = (NBTTagCompound) nbtpos.getTag("link_card_pos");
            if (nbtpos.hasKey("link_card_pos")) {
                BlockPos blockPos = BlockPos.fromLong(nbtpos.getLong("link_card_pos"));
                tooltip.add("Pos: " + blockPos.getX() + "/" + blockPos.getY() + "/" + blockPos.getX());
                return;
            }
        }
        tooltip.add("Pos: 未保存坐标");
    }
}
