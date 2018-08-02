package com.linecorp.beacamon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.beacamon.generator.ImageCarouselGenerator;
import com.linecorp.beacamon.generator.BeacamonGenerator;
import com.linecorp.beacamon.dto.UserInfoDto;
import com.linecorp.beacamon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserInfoService {

    Logger logger = LoggerFactory.getLogger(BeacamonGenerator.class);

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    BeacamonGenerator beacamonGenerator;

    @Autowired
    ImageCarouselGenerator imageCarouselGenerator;

    @Value("${NO_BEACAMON}")
    String NO_BEACAMON;


    @Value("${BEACAMON_ONE}")
    String BEACAMON_ONE;

    @Value("${BEACAMON_ONE_URL}")
    String BEACAMON_ONE_URL;


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
            String poke = beacamonGenerator.getBeacamon();
            return generateTemplate(poke, uuid);
        }
    }

    private Message generateTemplate (String poke, String uuid) {
        if(poke.equals(NO_BEACAMON)) {
            TextMessage textMessage = new TextMessage(NO_BEACAMON);
            return textMessage;
        } else {
            TemplateMessage templateMessage = imageCarouselGenerator.getTemplate(poke);
            saveUserInfo(uuid,BEACAMON_ONE, 1);
            return templateMessage;
        }

    }

    private TextMessage upgradeLevel (String uuid, String pokeName, Integer pokeLevel) {
        pokeLevel = pokeLevel + 1;
        saveUserInfo(uuid, pokeName, pokeLevel);
        updateRanking(uuid, pokeLevel);
        TextMessage textMessage =
                new TextMessage("Your Beacamon name: " + pokeName
                        + "\nYour Beacamon level: " + pokeLevel);
        return textMessage;
    }

    private void updateRanking (String uuid, Integer pokeLevel) {
        redisConnection.zadd("PokeLevel", pokeLevel, uuid);
    }
}
