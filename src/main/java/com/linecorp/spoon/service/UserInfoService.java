package com.linecorp.spoon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.spoon.generator.ImageCarouselGenerator;
import com.linecorp.spoon.generator.PokemonGenerator;
import com.linecorp.spoon.dto.UserInfoDto;
import com.linecorp.spoon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserInfoService {

    Logger logger = LoggerFactory.getLogger(PokemonGenerator.class);

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    PokemonGenerator pokemonGenerator;

    @Autowired
    ImageCarouselGenerator imageCarouselGenerator;

    @Value("${NO_POKE}")
    String NO_POKE;


    @Value("${POKE_ONE}")
    String POKE_ONE;

    @Value("${POKE_ONE_URL}")
    String POKE_ONE_URL;


    ObjectMapper mapper = new ObjectMapper();

    public void saveUserInfo (String uuid, String pokeName, Integer pokeLevel) {
        UserInfoDto userInfoDto = new UserInfoDto(pokeName, pokeLevel);
        try {
            String userInfoJson = mapper.writeValueAsString(userInfoDto);
            redisConnection.setRedis(uuid, userInfoJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Message getTemplateForUser (String uuid){

        try {
            String userInfo = redisConnection.getRedis(uuid);
            UserInfoDto userInfoDto = mapper.readValue(userInfo, UserInfoDto.class);
            TextMessage textMessage = upgradeLevel( uuid, userInfoDto.getPokeName(),userInfoDto.getPokeLevel());
            return textMessage;
        } catch (Exception e) {
            logger.warn("Warning:" + String.valueOf(e));
            String poke = pokemonGenerator.getPokemon();
            return generateTemplate(poke, uuid);
        }
    }

    private Message generateTemplate (String poke, String uuid) {
        if(poke.equals(NO_POKE)) {
            TextMessage textMessage = new TextMessage(NO_POKE);
            return textMessage;
        } else {
            TemplateMessage templateMessage = imageCarouselGenerator.getTemplate(poke);
            saveUserInfo(uuid,POKE_ONE, 1);
            return templateMessage;
        }

    }

    private TextMessage upgradeLevel (String uuid, String pokeName, Integer pokeLevel) {
        pokeLevel = pokeLevel + 1;
        saveUserInfo(uuid, pokeName, pokeLevel);
        TextMessage textMessage =
                new TextMessage("Your Pokemon name: " + pokeName
                        + "\nYour Pokemon level: " + pokeLevel);
        return textMessage;
    }
}
