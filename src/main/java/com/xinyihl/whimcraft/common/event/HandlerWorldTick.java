package com.xinyihl.whimcraft.common.event;

import com.xinyihl.whimcraft.Configurations;
import com.xinyihl.whimcraft.common.operators.WorldChunkUnloader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class HandlerWorldTick {

    private final Map<Integer, Integer> dimMap = new HashMap<>();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (Configurations.CHUNCK_CONFIG.enabled) {
            int dimension = event.world.provider.getDimension();
            if (Configurations.CHUNCK_CONFIG.dimlist.contains(dimension)) {
                if (dimMap.containsKey(dimension)) {
                    int tick = dimMap.get(dimension) + 1;
                    if (tick >= Configurations.CHUNCK_CONFIG.chunkUnloadDelay * 2) {
                        dimMap.put(dimension, 0);
                        WorldChunkUnloader worldChunkUnloader = new WorldChunkUnloader(event.world);
                        worldChunkUnloader.unloadChunks();
                    } else {
                        dimMap.put(dimension, tick);
                    }
                } else {
                    dimMap.put(dimension, 1);
                }
            }
        }
    }
}
