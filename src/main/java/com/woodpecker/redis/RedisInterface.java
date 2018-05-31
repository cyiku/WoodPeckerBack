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
        System.out.println(tableName + " 共有: " + stringSet.size() + "条消息");
        for(String str: stringSet) {
            //System.out.println(str);
            JSONObject outer = new JSONObject(str);
            //System.out.println(outer);
            //JSONObject inner = new JSONObject(outer.getString("content"));
            JSONObject inner = outer.getJSONObject("content");
            //System.out.println(inner);
            inner.put("contentType",outer.getString("type"));
            inner.put("keyword", tableName);
            result.add(inner);
        }
        return result;
    }
}
