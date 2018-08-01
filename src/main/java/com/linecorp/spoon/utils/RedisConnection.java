package com.linecorp.spoon.utils;

import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;


public class RedisConnection {

    private Jedis jedis;

    Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    public RedisConnection ( String REDIS_HOST,
                             int REDIS_PORT,
                             String REDIS_AUTH) {
        jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        jedis.auth(REDIS_AUTH);
        if(jedis.isConnected())
            logger.info("connected");
        jedis.ping();
        logger.info("init.....");
    }

    public void setRedis (String key, String value) {
        jedis.set(key, value);
        logger.info("set " + key + " as " + value);
        jedis.close();
    }

    public String getRedis (String key) {
        String value = jedis.get(key);
        logger.info("get " + key + " as " + value);
        jedis.close();
        return value;
    }

    @PreDestroy
    public void destroy() {
        jedis.close();
    }

}
