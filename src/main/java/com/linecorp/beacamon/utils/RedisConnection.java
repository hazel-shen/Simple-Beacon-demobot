package com.linecorp.beacamon.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.util.Set;

@Component
public class RedisConnection {

    Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    @Value("${redis.host}")
    String REDIS_HOST;

    @Value("${redis.port}")
    private int REDIS_PORT;

    @Value("${redis.auth}")
    private String REDIS_AUTH;

    private Jedis jedis = null;
    private static JedisPool jedisPool;

    public RedisConnection(String REDIS_HOST, Integer REDIS_PORT, String REDIS_AUTH) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(15);
        config.setMaxWaitMillis(10000);
        jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT, 2000, REDIS_AUTH);
        try {
            Jedis jedis = jedisPool.getResource();
            if(jedis.isConnected())
                logger.info("connected");
            logger.info("init.....");
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void setRedis (String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        logger.info("set " + key + " as " + value);
        jedis.close();
    }

    public String getRedis (String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(key);
        logger.info("get " + key + " as " + value);
        jedis.close();
        return value;
    }

    public void zadd (String field, Integer score,  String key) {
        Jedis jedis = jedisPool.getResource();
        logger.info("update field: " + field + " as { " + key + ", " + score + "}" );
        jedis.zadd(field, score, key);
        jedis.close();
    }

    public Set<String> getRank(String field) {
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.zrange(field, 0, 100000);
        logger.info("Ranking in: " + result);
        jedis.close();
        return result;
    }

    @PreDestroy
    public void destroy() {
        jedisPool.destroy();
    }

}
