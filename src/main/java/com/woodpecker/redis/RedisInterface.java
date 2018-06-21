package com.woodpecker.redis;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// import java.io.File;
// import java.io.PrintWriter;
// import java.io.FileOutputStream;
// import java.io.FileWriter;

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
        // String s = tableName + " from: " + startTime + " to:" + endTime + " 共有: " + stringSet.size() + "条消息";
        // try {
        //     File file = new File("test.txt");  
        //     FileWriter fw = new FileWriter(file, true);
        //     PrintWriter pw = new PrintWriter(fw);
        //     pw.println(s);// 在已有的基础上添加字符串  
        //     pw.flush();
        //     fw.flush();
        //     pw.close();
        //     fw.close();
        // } catch (Exception e) {
        //     e.printStackTrace();  
        // }  
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
