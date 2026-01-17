package com.xinyihl.whimcraft.common.items.placer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CablePlacerData {

    static final String NBT_START = "start";
    static final String NBT_END = "end";
    static final String NBT_WAYPOINTS = "waypoints";
    static final String NBT_PATH = "path";
    static final String NBT_CABLE_STACK = "cable";
    static final String NBT_OPTS = "opts";
    static final String OPT_ALLOW_REPLACE = "allow_replace";
    static final String OPT_MAX_NODES = "max_nodes";
    static final String OPT_RADIUS = "radius";
    private CablePlacerData() {
    }

    public static NBTTagCompound getRoot(ItemStack tool) {
        if (tool == null || tool.isEmpty() || !tool.hasTagCompound()) {
            return new NBTTagCompound();
        }
        return tool.getTagCompound().getCompoundTag(CablePlacer.NBT_ROOT);
    }

    public static NBTTagCompound getOrCreateRoot(ItemStack tool) {
        if (tool == null || tool.isEmpty()) {
            return new NBTTagCompound();
        }
        if (!tool.hasTagCompound()) {
            tool.setTagCompound(new NBTTagCompound());
        }
        return tool.getTagCompound().getCompoundTag(CablePlacer.NBT_ROOT);
    }

    public static void writeRoot(ItemStack tool, NBTTagCompound root) {
        if (tool == null || tool.isEmpty()) {
            return;
        }
        if (!tool.hasTagCompound()) {
            tool.setTagCompound(new NBTTagCompound());
        }
        tool.getTagCompound().setTag(CablePlacer.NBT_ROOT, root == null ? new NBTTagCompound() : root);
    }

    public static void clearAll(ItemStack tool) {
        if (tool == null || tool.isEmpty() || !tool.hasTagCompound()) {
            return;
        }
        NBTTagCompound root = tool.getTagCompound().getCompoundTag(CablePlacer.NBT_ROOT);
        root.removeTag(NBT_START);
        root.removeTag(NBT_END);
        root.removeTag(NBT_WAYPOINTS);
        root.removeTag(NBT_PATH);
        tool.getTagCompound().setTag(CablePlacer.NBT_ROOT, root);
    }

    public static void clearPath(NBTTagCompound root) {
        if (root != null) {
            root.removeTag(NBT_PATH);
        }
    }

    public static void setStart(NBTTagCompound root, BlockPos pos) {
        if (root != null && pos != null) {
            root.setLong(NBT_START, pos.toLong());
        }
    }

    public static void setEnd(NBTTagCompound root, BlockPos pos) {
        if (root != null && pos != null) {
            root.setLong(NBT_END, pos.toLong());
        }
    }

    public static BlockPos getStart(NBTTagCompound root) {
        return root != null && root.hasKey(NBT_START) ? BlockPos.fromLong(root.getLong(NBT_START)) : null;
    }

    public static BlockPos getEnd(NBTTagCompound root) {
        return root != null && root.hasKey(NBT_END) ? BlockPos.fromLong(root.getLong(NBT_END)) : null;
    }

    public static void appendWaypoint(NBTTagCompound root, BlockPos pos) {
        if (root == null || pos == null) {
            return;
        }
        NBTTagList list = root.getTagList(NBT_WAYPOINTS, 4); // long
        list.appendTag(new net.minecraft.nbt.NBTTagLong(pos.toLong()));
        root.setTag(NBT_WAYPOINTS, list);
        root.removeTag(NBT_PATH);
    }

    public static List<BlockPos> getWaypoints(NBTTagCompound root) {
        if (root == null || !root.hasKey(NBT_WAYPOINTS)) {
            return Collections.emptyList();
        }
        NBTTagList list = root.getTagList(NBT_WAYPOINTS, 4);
        List<BlockPos> out = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++) {
            out.add(BlockPos.fromLong(((net.minecraft.nbt.NBTTagLong) list.get(i)).getLong()));
        }
        return out;
    }

    public static List<BlockPos> getPath(NBTTagCompound root) {
        if (root == null || !root.hasKey(NBT_PATH)) {
            return Collections.emptyList();
        }
        NBTTagList list = root.getTagList(NBT_PATH, 4);
        List<BlockPos> out = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++) {
            out.add(BlockPos.fromLong(((net.minecraft.nbt.NBTTagLong) list.get(i)).getLong()));
        }
        return out;
    }

    public static void setPath(NBTTagCompound root, List<BlockPos> path) {
        if (root == null) {
            return;
        }
        NBTTagList list = new NBTTagList();
        if (path != null) {
            for (BlockPos p : path) {
                list.appendTag(new net.minecraft.nbt.NBTTagLong(p.toLong()));
            }
        }
        root.setTag(NBT_PATH, list);
    }

    public static ItemStack getCableStack(ItemStack tool) {
        return getCableStack(getRoot(tool));
    }

    public static ItemStack getCableStack(NBTTagCompound root) {
        if (root != null && root.hasKey(NBT_CABLE_STACK)) {
            return new ItemStack(root.getCompoundTag(NBT_CABLE_STACK));
        }
        return ItemStack.EMPTY;
    }

    public static void setCableStack(ItemStack tool, ItemStack cable) {
        NBTTagCompound root = getOrCreateRoot(tool);
        setCableStack(root, cable);
        writeRoot(tool, root);
    }

    public static void setCableStack(NBTTagCompound root, ItemStack cable) {
        if (root == null) {
            return;
        }
        if (cable == null || cable.isEmpty()) {
            root.removeTag(NBT_CABLE_STACK);
            return;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        cable.copy().writeToNBT(nbt);
        root.setTag(NBT_CABLE_STACK, nbt);
    }

    public static boolean getOptAllowReplace(ItemStack tool) {
        return getOptAllowReplace(getRoot(tool));
    }

    public static boolean getOptAllowReplace(NBTTagCompound root) {
        NBTTagCompound opts = root == null ? new NBTTagCompound() : root.getCompoundTag(NBT_OPTS);
        return opts.hasKey(OPT_ALLOW_REPLACE) && opts.getBoolean(OPT_ALLOW_REPLACE);
    }

    public static int getOptMaxNodes(NBTTagCompound root) {
        NBTTagCompound opts = root == null ? new NBTTagCompound() : root.getCompoundTag(NBT_OPTS);
        return opts.hasKey(OPT_MAX_NODES) ? opts.getInteger(OPT_MAX_NODES) : 20000;
    }

    public static int getOptRadius(NBTTagCompound root) {
        NBTTagCompound opts = root == null ? new NBTTagCompound() : root.getCompoundTag(NBT_OPTS);
        return opts.hasKey(OPT_RADIUS) ? opts.getInteger(OPT_RADIUS) : 24;
    }

    public static void setOptAllowReplace(ItemStack tool, boolean value) {
        setOptBoolean(tool, OPT_ALLOW_REPLACE, value);
    }

    public static void setOptBoolean(ItemStack tool, String key, boolean value) {
        if (tool == null || tool.isEmpty()) {
            return;
        }
        if (!tool.hasTagCompound()) {
            tool.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound root = tool.getTagCompound().getCompoundTag(CablePlacer.NBT_ROOT);
        NBTTagCompound opts = root.getCompoundTag(NBT_OPTS);
        opts.setBoolean(key, value);
        root.setTag(NBT_OPTS, opts);
        root.removeTag(NBT_PATH);
        tool.getTagCompound().setTag(CablePlacer.NBT_ROOT, root);
    }
}
