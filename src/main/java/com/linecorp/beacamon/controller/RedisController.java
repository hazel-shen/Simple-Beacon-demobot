package com.linecorp.beacamon.controller;

import com.linecorp.beacamon.utils.RedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    RedisConnection redisConnection;

    @GetMapping("/get/{id}")
    public @ResponseBody
    String getRedis(@PathVariable String id) {
        String s = redisConnection.getRedis(id);
        return s;
    }

    @GetMapping("/set/{id}")
    public @ResponseBody
    void setRedis(@PathVariable String id) {
        redisConnection.setRedis(id, "123");
    }
}
