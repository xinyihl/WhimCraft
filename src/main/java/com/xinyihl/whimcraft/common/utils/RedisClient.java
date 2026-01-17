package com.xinyihl.whimcraft.common.utils;

import com.xinyihl.whimcraft.Configurations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisClient {

    public static boolean isOnline;
    private static volatile JedisPool POOL;
    private static volatile String POOL_SIGNATURE;

    private RedisClient() {
    }

    private static String signature() {
        Configurations.RedisIOConfig c = Configurations.REDIS_IO_CONFIG;
        return c.host + ":" + c.port + ":" + c.database + ":" + (c.password == null ? "" : c.password);
    }

    private static JedisPool pool() {
        String sig = signature();
        JedisPool pool = POOL;
        if (pool == null || !sig.equals(POOL_SIGNATURE)) {
            synchronized (RedisClient.class) {
                if (POOL == null || !sig.equals(POOL_SIGNATURE)) {
                    if (POOL != null) {
                        try {
                            POOL.close();
                        } catch (Exception ignored) {
                        }
                    }
                    JedisPoolConfig cfg = new JedisPoolConfig();
                    cfg.setMaxTotal(8);
                    cfg.setMaxIdle(8);
                    cfg.setMinIdle(0);
                    POOL = new JedisPool(cfg, Configurations.REDIS_IO_CONFIG.host, Configurations.REDIS_IO_CONFIG.port, 2000);
                    POOL_SIGNATURE = sig;
                }
                pool = POOL;
            }
        }
        return pool;
    }

    public static Jedis getJedis() {
        Jedis jedis = pool().getResource();
        Configurations.RedisIOConfig c = Configurations.REDIS_IO_CONFIG;
        if (c.password != null && !c.password.trim().isEmpty()) {
            jedis.auth(c.password);
        }
        if (c.database != 0) {
            jedis.select(c.database);
        }
        return jedis;
    }
}
