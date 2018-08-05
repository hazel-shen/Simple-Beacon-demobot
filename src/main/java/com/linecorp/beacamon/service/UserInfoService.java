package com.linecorp.beacamon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.beacamon.dto.BeacamonDto;
import com.linecorp.bot.model.message.TextMessage;
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

    Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    BeacamonGenerator beacamonGenerator;

    @Value("${NO_BEACAMON}")
    String NO_BEACAMON;

    Integer INIT_LEVEL = 1;

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

    public Boolean isHasBeacaMon (String uuid) {
        try {
            String userInfo = redisConnection.getRedis(uuid);
            if (userInfo.equals(null)) {
                logger.info("NOTING");
            }
            logger.info( uuid + " has BeacaMon" );
            return true;
        } catch (Exception e) {
            logger.info( uuid + " doesn't have BeacaMon" );
            return false;
        }
    }

    public BeacamonDto getBeacamonName (String uuid){

        try {
            String userInfo = redisConnection.getRedis(uuid);
            logger.info("Read from userinfo: " + userInfo);
            if (userInfo.equals(null) || userInfo.equals("null")) {
                logger.info("NOTING");
            }
            BeacamonDto beacamonDto = new BeacamonDto(NO_BEACAMON, "https://f4.bcbits.com/img/a0252633309_10.jpg");
            return beacamonDto;
        } catch (Exception e) {
            e.printStackTrace();
            BeacamonDto beacamonDto = beacamonGenerator.getBeacamon();
            saveUserInfo(uuid, beacamonDto.getBeacamonName(), INIT_LEVEL);
            return beacamonDto;
        }
    }
}
