package com.xinyihl.whimcraft.common.operators;

import com.xinyihl.whimcraft.Configurations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class WorldChunkUnloader {

    private static final Logger log = LogManager.getLogger(WorldChunkUnloader.class);
    private final World world;
    private HashSet<ChunkPos> chunksToUnload;

    public WorldChunkUnloader(World world) {
        this.world = world;
    }

    private HashSet<ChunkPos> groupedChunksFinder(HashSet<ChunkPos> loadedChunks, ChunkPos seed, int radiusLimit) {
        LinkedList<ChunkPos> queue = new LinkedList<>();
        HashSet<ChunkPos> groupedChunks = new HashSet<>();
        if (!loadedChunks.contains(seed)) return groupedChunks;
        queue.add(seed);
        while (!queue.isEmpty()) {
            ChunkPos chunk = queue.remove();
            if (!groupedChunks.contains(chunk)) {
                int west, east;
                for (west = chunk.x;
                     loadedChunks.contains(new ChunkPos(west - 1, chunk.z))
                             && (radiusLimit == 0 || Math.abs(west - 1 - seed.x) <= radiusLimit);
                     --west)
                    ;
                for (east = chunk.x;
                     loadedChunks.contains(new ChunkPos(east + 1, chunk.z))
                             && (radiusLimit == 0 || Math.abs(east + 1 - seed.x) <= radiusLimit);
                     ++east)
                    ;
                for (int x = west; x <= east; ++x) {
                    groupedChunks.add(new ChunkPos(x, chunk.z));
                    if (loadedChunks.contains(new ChunkPos(x, chunk.z + 1))
                            && (radiusLimit == 0 || Math.abs(chunk.z + 1 - seed.z) <= radiusLimit)) {
                        queue.add(new ChunkPos(x, chunk.z + 1));
                    }
                    if (loadedChunks.contains(new ChunkPos(x, chunk.z - 1))
                            && (radiusLimit == 0 || Math.abs(chunk.z - 1 - seed.z) <= radiusLimit)) {
                        queue.add(new ChunkPos(x, chunk.z - 1));
                    }
                }
            }
        }
        return groupedChunks;
    }

    private void populateChunksToUnload() {
        chunksToUnload = new HashSet<>();
        if (world.getChunkProvider() instanceof ChunkProviderServer) {
            HashSet<ChunkPos> loadedChunks = new HashSet<>();
            HashSet<ChunkPos> playerLoadedChunks = new HashSet<>();
            HashSet<ChunkPos> forceLoadedChunks = new HashSet<>();
            HashSet<ChunkPos> spawnLoadedChunks = new HashSet<>();
            List<EntityPlayer> listPlayers = world.playerEntities;
            int radiusLimit;
            final int CHUNK_WIDTH = 16;
            final int PLAYER_RADIUS = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getViewDistance();
            final int TICKET_RADIUS = 0;
            final int SPAWN_RADIUS = 8;
            int PLAYER_LIMIT = Configurations.CHUNCK_CONFIG.pradius;
            int TICKET_LIMIT = Configurations.CHUNCK_CONFIG.tradius;
            int SPAWN_LIMIT = Configurations.CHUNCK_CONFIG.sradius;
            for (Chunk chunk : ((ChunkProviderServer) world.getChunkProvider()).getLoadedChunks()) {
                loadedChunks.add(chunk.getPos());
            }
            radiusLimit = (int) Math.ceil(PLAYER_RADIUS + PLAYER_LIMIT);
            for (EntityPlayer player : listPlayers) {
                if (!(player instanceof FakePlayer)) {
                    ChunkPos playerChunkCoords = new ChunkPos(player.chunkCoordX, player.chunkCoordZ);
                    playerLoadedChunks.addAll(groupedChunksFinder(loadedChunks, playerChunkCoords, radiusLimit));
                }
            }
            radiusLimit = (int) Math.ceil(TICKET_RADIUS + TICKET_LIMIT);
            for (ChunkPos coord : world.getPersistentChunks().keySet()) {
                forceLoadedChunks.addAll(groupedChunksFinder(loadedChunks, coord, radiusLimit));
            }
            radiusLimit = (int) Math.ceil(SPAWN_RADIUS + SPAWN_LIMIT);
            if (world.provider.canRespawnHere() && world.provider.getDimensionType().shouldLoadSpawn()) {
                ChunkPos spawnChunkCoords = new ChunkPos(
                        (int) Math.floor((double) world.getSpawnPoint().getX() / CHUNK_WIDTH),
                        (int) Math.floor((double) world.getSpawnPoint().getZ() / CHUNK_WIDTH));
                spawnLoadedChunks.addAll(groupedChunksFinder(loadedChunks, spawnChunkCoords, radiusLimit));
            }
            for (ChunkPos coord : loadedChunks) {
                if (!playerLoadedChunks.contains(coord)
                        && !forceLoadedChunks.contains(coord)
                        && !spawnLoadedChunks.contains(coord)) {
                    chunksToUnload.add(coord);
                }
            }
        }
    }

    public void unloadChunks() {
        long initialTime = MinecraftServer.getCurrentTimeMillis();
        populateChunksToUnload();
        if (this.world.getChunkProvider() instanceof ChunkProviderServer) {
            ChunkProviderServer chunkProviderServer = (ChunkProviderServer) this.world.getChunkProvider();
            for (ChunkPos coord : chunksToUnload) {
                Chunk chunk = chunkProviderServer.getLoadedChunk(coord.x, coord.z);
                if (chunk != null) {
                    chunkProviderServer.queueUnload(chunk);
                }
            }
        }
        if (Configurations.CHUNCK_CONFIG.debug) {
            log.log(Level.INFO, "Queued " + chunksToUnload.size()
                    + " chunks for unload in " + this.world.provider.getDimensionType().getName()
                    + " (" + this.world.provider.getDimension()
                    + ") in " + (MinecraftServer.getCurrentTimeMillis() - initialTime)
                    + " milliseconds.");
        }
    }
}
