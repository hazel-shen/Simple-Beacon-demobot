package com.linecorp.spoon.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.spoon.dto.EventInfoDto;
import com.linecorp.spoon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JoinCollectService {

    @Autowired
    RedisConnection redisConnection;

    ObjectMapper mapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    public void setEventHolder (String eventName, String holder) throws IOException {
        EventInfoDto eventInfoDto = new EventInfoDto();
        eventInfoDto.setHolder(holder);
        String eventInfoJson = mapper.writeValueAsString(eventInfoDto);
        logger.info("eventInfo initialized... " + eventInfoJson);
        redisConnection.setRedis(eventName, eventInfoJson);
    }

    public void setEventjoiner (String eventName, String joiner) throws IOException {
        EventInfoDto eventInfoDto = this.getEventData(eventName);
        List<String> joinerList = eventInfoDto.getJoinerList();
        joinerList.add(joiner);
        eventInfoDto.setJoinerList(joinerList);
        String eventInfoJson = mapper.writeValueAsString(eventInfoDto);
        logger.info("eventInfo joiner list... " + eventInfoJson);
        redisConnection.setRedis(eventName, eventInfoJson);
    }

    public EventInfoDto getEventData (String eventName) throws IOException {
        String eventInfoJson = redisConnection.getRedis(eventName);
        EventInfoDto eventInfoDto = mapper.readValue(eventInfoJson, EventInfoDto.class);
        logger.info("Get eventInfo data.. " + eventInfoJson);
        return eventInfoDto;
    }


}
