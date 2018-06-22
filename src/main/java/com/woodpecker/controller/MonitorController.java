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
    /**
     * 监控页面的Controller
     * 主要有两个函数：
     * Monitor：正常监控请求
     * last20：第一次请求，得到最近20条(20位变量)
     */
    @Resource
    private RedisInterface redisInterface;

    @Resource
    UserService userService;

    @Value("${spring.es.host}")
    private String esHost;

    @Value("${spring.es.port}")
    private String esPort;

    // 时间间隔，获取近30s内的消息，redis里存放的是近20s内的
    private final int monitorInterval = 30;

    @RequestMapping(value = "/monitor", method = RequestMethod.POST)
    public String monitor(@RequestBody String info) {
        /**
         * 正常监控, 每隔一段时间请求一次
         */

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // keywordName：关键字名称；tableName：关键字在redis中键值对的名称
            String keywordName = (String)jsonObject.get("name");
            String tableName = keywordName + "_cache";

            // 设置成东八区时间，读取startTime-now这段时间内的消息
            Calendar curTime = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            curTime.setTimeZone(timeZone);
            Calendar startTime = (Calendar)curTime.clone();
            startTime.add(Calendar.SECOND,-monitorInterval);
            //startTime.add(Calendar.DATE, -10);

            // 将Calendar置为double
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Double score1 = Double.parseDouble(format.format(startTime.getTime()));
            Double score2 = Double.parseDouble(format.format(curTime.getTime()));

            // 从redis中获取数据
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
        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);
            // keywordName：关键字名称
            String keywordName = (String)jsonObject.get("name");
            // count: 消息条数
            int count = 10;
            // 读取来自微博的最新的10条，原因是微博爬虫比较给力
            List<String> type = new LinkedList<>();
            type.add("weibo");
            // 使用es读取数据
            List<JSONObject> searchData = EsSearch.esSearch(esHost, esPort, 0, count, keywordName, type);
            // 将读取后的数据中的文本类型设置为weibo
            for(int i = 0; i < searchData.size(); ++i) {
                searchData.get(i).put("contentType", "weibo");
            }
            result.put("data", searchData);
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }

        return JSONResult.fillResultString(status,message,result);
    }
}
