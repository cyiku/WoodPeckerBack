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

    public void showAll() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            for(String o:keys) {
                String val = redisTemplate.opsForValue().get(o);
                System.out.println(o + " " + val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAndDeleteAll() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            for(String o:keys) {
                String val = redisTemplate.opsForValue().get(o);
                System.out.println(o + " " + val);
                redisTemplate.delete(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<JSONObject> getData(String tableName,Double startTime,Double endTime) {
        List<JSONObject> result = new ArrayList<>();
        ZSetOperations<String, String> zOps = redisTemplate.opsForZSet();
        Set<String> stringSet = zOps.rangeByScore(tableName,startTime,endTime);
        for(String str: stringSet) {
            result.add(new JSONObject(str));
        }
        return result;
    }
}
