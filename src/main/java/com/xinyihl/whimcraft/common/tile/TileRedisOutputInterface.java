package com.xinyihl.whimcraft.common.tile;

import com.xinyihl.whimcraft.common.redis.RedisIoClient;
import com.xinyihl.whimcraft.common.tile.base.TileRedisInterfaceBase;
import redis.clients.jedis.Jedis;

public class TileRedisOutputInterface extends TileRedisInterfaceBase {

	@Override
	public String getRedisKeyType() {
		return "output";
	}

	@Override
	protected void doRedisSync() throws Exception {
		try (Jedis jedis = RedisIoClient.getJedis()) {
			popStacksFromRedisQueue(jedis, redisKey(), inventory);
		}
	}
}
