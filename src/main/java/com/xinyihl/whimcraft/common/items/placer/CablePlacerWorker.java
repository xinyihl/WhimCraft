package com.xinyihl.whimcraft.common.items.placer;

import com.xinyihl.whimcraft.api.CableCompatManager;
import com.xinyihl.whimcraft.common.utils.CablePathfinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CablePlacerWorker {

    private CablePlacerWorker() {
    }

    public static List<BlockPos> planPath(World world, NBTTagCompound root) {
        BlockPos start = CablePlacerData.getStart(root);
        BlockPos end = CablePlacerData.getEnd(root);
        if (start == null || end == null) {
            return Collections.emptyList();
        }

        List<BlockPos> points = new ArrayList<>();
        points.add(start);
        points.addAll(CablePlacerData.getWaypoints(root));
        points.add(end);

        int maxNodes = CablePlacerData.getOptMaxNodes(root);
        int radius = CablePlacerData.getOptRadius(root);
        boolean allowReplace = CablePlacerData.getOptAllowReplace(root);

        List<BlockPos> out = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            BlockPos a = points.get(i);
            BlockPos b = points.get(i + 1);
            List<BlockPos> seg = CablePathfinder.findPath(world, a, b, radius, maxNodes, allowReplace);
            if (seg.isEmpty()) {
                return Collections.emptyList();
            }
            if (!out.isEmpty() && out.get(out.size() - 1).equals(seg.get(0))) {
                seg = seg.subList(1, seg.size());
            }
            out.addAll(seg);
        }

        return out;
    }

    public static int placeAll(World world, EntityPlayer player, EnumHand toolHand, NBTTagCompound root, List<BlockPos> path) {
        ItemStack cableTemplate = CablePlacerData.getCableStack(root);
        if (cableTemplate.isEmpty()) {
            return 0;
        }

        boolean allowReplace = CablePlacerData.getOptAllowReplace(root);

        int placed = 0;
        for (BlockPos placePos : path) {
            if (!allowReplace && !world.getBlockState(placePos).getMaterial().isReplaceable()) {
                continue;
            }

            ItemStack placingStack;
            if (!player.capabilities.isCreativeMode) {
                placingStack = takeOneFromInventory(player, cableTemplate);
                if (placingStack.isEmpty()) {
                    player.sendStatusMessage(new TextComponentTranslation("message.whimcraft.cable_placer.no_material"), true);
                    break;
                }
            } else {
                placingStack = cableTemplate.copy();
                placingStack.setCount(1);
            }

            boolean ok = CableCompatManager.placeCable(new CablePlaceContext(world, player, toolHand, placePos, placingStack, allowReplace));
            if (ok) {
                placed++;
            } else if (!player.capabilities.isCreativeMode) {
                // 放置失败：生存模式退回已预扣的材料
                ItemStack rollback = cableTemplate.copy();
                rollback.setCount(1);
                player.inventory.addItemStackToInventory(rollback);
            }
        }

        return placed;
    }

    private static ItemStack takeOneFromInventory(EntityPlayer player, ItemStack template) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack in = player.inventory.getStackInSlot(i);
            if (ItemStack.areItemsEqual(template, in) && ItemStack.areItemStackTagsEqual(template, in)) {
                ItemStack out = in.copy();
                out.setCount(1);
                in.shrink(1);
                if (in.getCount() <= 0) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                return out;
            }
        }
        return ItemStack.EMPTY;
    }
}
