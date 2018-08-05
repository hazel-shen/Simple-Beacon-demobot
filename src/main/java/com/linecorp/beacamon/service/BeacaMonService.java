package com.linecorp.beacamon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.beacamon.dto.UserInfoDto;
import com.linecorp.beacamon.utils.RedisConnection;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Component
public class BeacaMonService {

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    UserInfoService userInfoService;

    ObjectMapper mapper = new ObjectMapper();


    public int getRank (String field, String uuid ) {
        Set<String> ranking = redisConnection.getRank(field);
        int rank = new ArrayList<>(ranking).indexOf(uuid) + 1;
        return rank;
    }

    public UserInfoDto upgradeLevel (String uuid) throws IOException {
        String userIndfo = redisConnection.getRedis(uuid);
        UserInfoDto userInfoDto = mapper.readValue(userIndfo, UserInfoDto.class);
        Integer newLevel = userInfoDto.getPokeLevel() + 1;
        userInfoService.saveUserInfo(uuid, userInfoDto.getPokeName(), newLevel);
        updateRanking(uuid, newLevel);
        return userInfoDto;

    }

    private void updateRanking (String uuid, Integer pokeLevel) {
        redisConnection.zadd("BeacamonLevel", pokeLevel, uuid);
    }
}
