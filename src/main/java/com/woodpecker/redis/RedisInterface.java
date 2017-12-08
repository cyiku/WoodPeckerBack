package com.woodpecker.redis;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisInterface {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public String showAll() {
        String result = "";
        try {
            Set<String> keys = redisTemplate.keys("*");
            System.out.println(keys.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<JSONObject> getData(String tableName,Double startTime,Double endTime) {
        List<JSONObject> result = new ArrayList<>();
        ZSetOperations<String, String> zOps = redisTemplate.opsForZSet();
        Set<String> stringSet = zOps.rangeByScore(tableName,startTime,endTime);
        for(String str: stringSet) {
            JSONObject outer = new JSONObject(str);
            JSONObject inner = new JSONObject(outer.getString("content"));
            inner.put("contentType",outer.getString("type"));
            result.add(inner);
        }
        return result;
    }
}