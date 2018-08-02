package com.linecorp.beacamon.generator;

import com.linecorp.beacamon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
public class BeacamonGenerator {

    Logger logger = LoggerFactory.getLogger(BeacamonGenerator.class);

    @Autowired
    RedisConnection redisConnection;

    @Value("${BEACAMON_ONE}")
    String BEACAMON_ONE;

    @Value("${BEACAMON_ONE_URL}")
    String BEACAMON_ONE_URL;

    @Value("${NO_BEACAMON}")
    String NO_BEACAMON;

    public String getBeacamon() {
        Random random = new Random();
        Integer pokeNumber = random.nextInt(50) + 1;
        logger.info("Poke generated" + pokeNumber);
        if (pokeNumber%2 == 0)
            return redisConnection.getRedis(BEACAMON_ONE);
        return NO_BEACAMON;
    }


    @PostConstruct
    private void setBeacamon() {
        redisConnection.setRedis(BEACAMON_ONE, BEACAMON_ONE_URL);
        logger.info("Set NO Beacamon information: " + NO_BEACAMON);
    }
}
