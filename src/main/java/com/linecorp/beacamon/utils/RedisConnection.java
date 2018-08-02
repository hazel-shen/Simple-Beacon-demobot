package com.linecorp.beacamon.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

@Component
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

    public void zadd (String field, Integer score,  String key) {
        logger.info("update field: " + field + " as { " + key + ", " + score + "}" );
        jedis.zadd(field, score, key);
        jedis.close();
    }

    @PreDestroy
    public void destroy() {
        jedis.close();
    }

}
