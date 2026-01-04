package com.xinyihl.whimcraft.common.tile;

import com.xinyihl.whimcraft.common.redis.RedisIoClient;
import com.xinyihl.whimcraft.common.tile.base.TileRedisInterfaceBase;
import redis.clients.jedis.Jedis;

public class TileRedisInputInterface extends TileRedisInterfaceBase {

	@Override
	public String getRedisKeyType() {
		return "input";
	}

	@Override
	protected void doRedisSync() throws Exception {
		try (Jedis jedis = RedisIoClient.getJedis()) {
			pushStacksToRedisQueue(jedis, redisKey(), inventory);
		}
	}
}
