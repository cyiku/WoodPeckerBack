package com.woodpecker.redis;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// 每次读取，将redis中的消息数目写入文件 
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
        // 读取redis操作
        List<JSONObject> result = new ArrayList<>();
        ZSetOperations<String, String> zOps = redisTemplate.opsForZSet();
        Set<String> stringSet = zOps.rangeByScore(tableName,startTime,endTime);

        // 每次读取，将redis中的消息数目写入文件 
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
        
        // 从JSONObject中选择对展示有用的信息，组成每条消息内容
        for(String str: stringSet) {
            JSONObject outer = new JSONObject(str);
            JSONObject inner = outer.getJSONObject("content");
            inner.put("contentType",outer.getString("type"));
            inner.put("keyword", tableName);
            result.add(inner);
        }
        return result;
    }
}
