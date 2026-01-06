package com.xinyihl.whimcraft.common.tile;

import com.xinyihl.whimcraft.common.redis.RedisClient;
import com.xinyihl.whimcraft.common.tile.base.TileRedisInterfaceBase;
import redis.clients.jedis.Jedis;

public class TileRedisInputInterface extends TileRedisInterfaceBase {

    @Override
    public String getType() {
        return "input";
    }

    @Override
    protected void doSync() throws Exception {
        try (Jedis jedis = RedisClient.getJedis()) {
            this.pushStacksToRedisQueue(jedis, redisKey());
        }
    }
}
