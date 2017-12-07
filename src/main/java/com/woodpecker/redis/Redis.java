package com.woodpecker.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class Redis {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public String test() {
        try {
            redisTemplate.opsForValue().set("test", "testvalue");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
