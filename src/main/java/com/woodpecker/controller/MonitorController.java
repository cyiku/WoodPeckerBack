package com.woodpecker.controller;

import com.woodpecker.redis.RedisInterface;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class MonitorController {
    @Resource
    private RedisInterface redisInterface;

    private final int monitorInterval = 20;

    @RequestMapping(value = "/monitor", method = RequestMethod.POST)
    public String monitor(@RequestBody String info) {
        System.out.println("MONITOR!!");
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("name");
            String tableName = keywordName + "_cache";

            Calendar curTime = new GregorianCalendar();
            Calendar startTime = (Calendar)curTime.clone();
            //startTime.add(Calendar.SECOND,-monitorInterval);
            startTime.add(Calendar.DATE, -1);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Double score1 = Double.parseDouble(format.format(startTime.getTime()));
            Double score2 = Double.parseDouble(format.format(curTime.getTime()));

            result.put("data",redisInterface.getData(tableName,score1,score2));
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }

        return JSONResult.fillResultString(status,message,result);
    }
}
