package com.xinyihl.whimcraft.common.items.placer;

import com.xinyihl.whimcraft.Tags;
import com.xinyihl.whimcraft.WhimCraft;
import com.xinyihl.whimcraft.common.event.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

import static com.xinyihl.whimcraft.common.init.IB.CREATIVE_TAB;

public class CablePlacer extends Item {

    public static final String NBT_ROOT = "cable_placer";

    public CablePlacer() {
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setCreativeTab(CREATIVE_TAB);
        this.setRegistryName(new ResourceLocation(Tags.MOD_ID, "cable_placer"));
        this.setTranslationKey(Tags.MOD_ID + ".cable_placer");
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return EnumActionResult.SUCCESS;
        ItemStack tool = player.getHeldItem(hand);
        BlockPos target = pos.offset(facing);
        NBTTagCompound root = CablePlacerData.getOrCreateRoot(tool);
        if (player.isSneaking()) {
            CablePlacerData.appendWaypoint(root, target);
            CablePlacerData.writeRoot(tool, root);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.waypoint", target.getX(), target.getY(), target.getZ()), true);
            return EnumActionResult.SUCCESS;
        }
        BlockPos start = CablePlacerData.getStart(root);
        BlockPos end = CablePlacerData.getEnd(root);
        if (start == null) {
            CablePlacerData.setStart(root, target);
            CablePlacerData.clearPath(root);
            CablePlacerData.writeRoot(tool, root);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.start", target.getX(), target.getY(), target.getZ()), true);
            return EnumActionResult.SUCCESS;
        }
        if (end == null) {
            CablePlacerData.setEnd(root, target);
            CablePlacerData.writeRoot(tool, root);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.end", target.getX(), target.getY(), target.getZ()), true);
            List<BlockPos> planned = CablePlacerWorker.planPath(worldIn, root);
            if (planned.isEmpty()) {
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.plan_fail"), true);
            } else {
                CablePlacerData.setPath(root, planned);
                CablePlacerData.writeRoot(tool, root);
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.plan_ok", planned.size()), true);
            }
            return EnumActionResult.SUCCESS;
        }
        CablePlacerData.setEnd(root, target);
        List<BlockPos> planned = CablePlacerWorker.planPath(worldIn, root);
        if (planned.isEmpty()) {
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.plan_fail"), true);
        } else {
            CablePlacerData.setPath(root, planned);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.plan_ok", planned.size()), true);
        }
        CablePlacerData.writeRoot(tool, root);
        return EnumActionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack tool = player.getHeldItem(hand);
        if (!world.isRemote) {
            if (player.isSneaking()) {
                CablePlacerData.clearAll(tool);
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.clear"), true);
                return ActionResult.newResult(EnumActionResult.SUCCESS, tool);
            }

            NBTTagCompound root = CablePlacerData.getOrCreateRoot(tool);
            BlockPos start = CablePlacerData.getStart(root);
            BlockPos end = CablePlacerData.getEnd(root);
            ItemStack cable = CablePlacerData.getCableStack(root);
            if (start == null || end == null || cable.isEmpty()) {
                player.openGui(WhimCraft.instance, GuiHandler.CABLE_PLACER_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                return ActionResult.newResult(EnumActionResult.SUCCESS, tool);
            }
            List<BlockPos> path = CablePlacerData.getPath(root);
            if (path.isEmpty()) {
                path = CablePlacerWorker.planPath(world, root);
                if (!path.isEmpty()) {
                    CablePlacerData.setPath(root, path);
                    CablePlacerData.writeRoot(tool, root);
                }
            }
            if (path.isEmpty()) {
                player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.plan_fail"), true);
                return ActionResult.newResult(EnumActionResult.SUCCESS, tool);
            }
            int placed = CablePlacerWorker.placeAll(world, player, hand, root, path);
            player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.placed", placed, path.size()), true);
            CablePlacerData.writeRoot(tool, root);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, tool);
    }
}
