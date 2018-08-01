package com.linecorp.spoon.configs;

import com.linecorp.spoon.utils.RedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    String REDIS_HOST;

    @Value("${redis.port}")
    private int REDIS_PORT;

    @Value("${redis.auth}")
    private String REDIS_AUTH;


    @Bean
    RedisConnection redisConnection() {
        return new RedisConnection(REDIS_HOST, REDIS_PORT, REDIS_AUTH);
    }
}
