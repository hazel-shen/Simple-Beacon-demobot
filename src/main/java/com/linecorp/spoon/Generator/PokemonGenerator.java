package com.linecorp.spoon.Generator;

import com.linecorp.spoon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
public class PokemonGenerator {

    Logger logger = LoggerFactory.getLogger(PokemonGenerator.class);

    @Autowired
    RedisConnection redisConnection;

    @Value("${POKE_ONE}")
    String POKE_ONE;

    @Value("${POKE_ONE_URL}")
    String POKE_ONE_URL;

    @Value("${NO_POKE}")
    String NO_POKE;

    public String getPokemon() {
        Random random = new Random();
        Integer pokeNumber = random.nextInt(50) + 1;
        logger.info("Poke generated" + pokeNumber);
        if (pokeNumber%2 == 0)
            return redisConnection.getRedis(POKE_ONE);
        return NO_POKE;
    }


    @PostConstruct
    private void setPokemon() {
        redisConnection.setRedis(POKE_ONE, POKE_ONE_URL);
        logger.info("Set NO Pokemon information: " + NO_POKE);
    }
}
