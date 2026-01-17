package com.xinyihl.whimcraft.common.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public final class CablePathfinder {

    // 让路径“尽量直”：在保证步数最短的前提下，优先选择转角更少的路径。
    // 用整数缩放避免 float：总成本 = steps * STEP_COST + turns * TURN_COST。
    private static final int STEP_COST = 1000;
    private static final int TURN_COST = 1;

    private CablePathfinder() {
    }

    public static List<BlockPos> findPath(World world, BlockPos start, BlockPos goal, int radius, int maxNodes, boolean allowReplace) {
        if (start.equals(goal)) {
            return Collections.singletonList(start);
        }
        int minX = Math.min(start.getX(), goal.getX()) - radius;
        int maxX = Math.max(start.getX(), goal.getX()) + radius;
        int minY = Math.max(0, Math.min(start.getY(), goal.getY()) - radius);
        int maxY = Math.min(255, Math.max(start.getY(), goal.getY()) + radius);
        int minZ = Math.min(start.getZ(), goal.getZ()) - radius;
        int maxZ = Math.max(start.getZ(), goal.getZ()) + radius;
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt((Node a) -> a.f).thenComparingInt(a -> a.turns).thenComparingInt(a -> a.h));
        @SuppressWarnings("unchecked")
        Map<Long, Node>[] bestByDir = new Map[7];
        for (int i = 0; i < bestByDir.length; i++) {
            bestByDir[i] = new HashMap<>();
        }
        int startH = h(start, goal) * STEP_COST;
        Node startNode = new Node(start, null, 0, 0, 6, startH, startH);
        open.add(startNode);
        bestByDir[6].put(start.toLong(), startNode);
        int expanded = 0;
        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.pos.equals(goal)) {
                return reconstruct(cur);
            }
            if (++expanded > maxNodes) {
                return Collections.emptyList();
            }
            for (BlockPos next : neighbors(cur.pos)) {
                int nextDir = dirIndex(cur.pos, next);
                if (next.getX() < minX || next.getX() > maxX || next.getY() < minY || next.getY() > maxY || next.getZ() < minZ || next.getZ() > maxZ) {
                    continue;
                }
                if (!isPassable(world, next, allowReplace) && !next.equals(goal)) {
                    continue;
                }
                int addTurn = (cur.dir != 6 && nextDir != cur.dir) ? 1 : 0;
                int turns = cur.turns + addTurn;
                int steps = cur.steps + 1;
                int g = steps * STEP_COST + turns * TURN_COST;
                Map<Long, Node> bestMap = bestByDir[nextDir];
                Node prevBest = bestMap.get(next.toLong());
                if (prevBest != null) {
                    if (g > prevBest.g) continue;
                    if (g == prevBest.g && turns >= prevBest.turns) continue;
                }
                int hn = h(next, goal) * STEP_COST;
                Node n = new Node(next, cur, steps, turns, nextDir, g, g + hn);
                bestMap.put(next.toLong(), n);
                open.add(n);
            }
        }
        return Collections.emptyList();
    }

    private static boolean isPassable(World world, BlockPos pos, boolean allowReplace) {
        if (world.isAirBlock(pos)) return true;
        if (allowReplace) {
            return world.getBlockState(pos).getMaterial().isReplaceable();
        }
        return false;
    }

    private static int h(BlockPos a, BlockPos b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) + Math.abs(a.getZ() - b.getZ());
    }

    private static List<BlockPos> reconstruct(Node node) {
        List<BlockPos> out = new ArrayList<>();
        Node cur = node;
        while (cur != null) {
            out.add(cur.pos);
            cur = cur.parent;
        }
        Collections.reverse(out);
        return out;
    }

    private static List<BlockPos> neighbors(BlockPos p) {
        return Arrays.asList(
                p.east(),
                p.west(),
                p.south(),
                p.north(),
                p.up(),
                p.down()
        );
    }

    private static int dirIndex(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();
        if (dx == 1) return 0;   // east
        if (dx == -1) return 1;  // west
        if (dz == 1) return 2;   // south
        if (dz == -1) return 3;  // north
        if (dy == 1) return 4;   // up
        return 5;                // down
    }

    private static final class Node {
        private final BlockPos pos;
        private final Node parent;
        private final int steps;
        private final int turns;
        private final int dir;
        private final int h;
        private final int g;
        private final int f;

        private Node(BlockPos pos, Node parent, int steps, int turns, int dir, int g, int f) {
            this.pos = pos;
            this.parent = parent;
            this.steps = steps;
            this.turns = turns;
            this.dir = dir;
            this.h = f - g;
            this.g = g;
            this.f = f;
        }
    }
}
