package com.woodpecker.controller;

import com.woodpecker.redis.RedisInterface;
import com.woodpecker.util.JSONResult;
import com.woodpecker.util.EsSearch;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class MonitorController {
    @Resource
    private RedisInterface redisInterface;

    @Resource
    UserService userService;

    @Value("${spring.es.host}")
    private String esHost;

    @Value("${spring.es.port}")
    private String esPort;

    private final int monitorInterval = 20;

    @RequestMapping(value = "/monitor", method = RequestMethod.POST)
    public String monitor(@RequestBody String info) {
        /**
         * 正常监控, 每隔一段时间请求一次
         */
        System.out.println("MONITOR!!");
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("name");
            String tableName = keywordName + "_cache";

            Calendar curTime = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            curTime.setTimeZone(timeZone);
            Calendar startTime = (Calendar)curTime.clone();
            startTime.add(Calendar.SECOND,-monitorInterval);
            //startTime.add(Calendar.DATE, -10);

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

    @RequestMapping(value = "/last20", method = RequestMethod.POST)
    public String last20(@RequestBody String info) {
        /**
         * 获取最新的20条
         */
        System.out.println("MONITOR!!");
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("name");

            int count = 10;
            List<String> type = new LinkedList<>();
            type.add("weibo");
            result.put("data", EsSearch.esSearch(esHost, esPort, 0, count, keywordName, type, true));
            //result.put("data",searchLast5FromBaiduSearch(keywordName, count));
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }

        return JSONResult.fillResultString(status,message,result);
    }
}
