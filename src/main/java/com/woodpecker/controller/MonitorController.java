package com.woodpecker.controller;

import com.woodpecker.redis.RedisInterface;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.InetAddress;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;     //map to json


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

    private List<JSONObject> searchLast5FromBaiduSearch(String keyword, int count) {
        /**
         * 从百度搜索里查询最新的5条
         */
        String host = esHost;
        int port = Integer.parseInt(esPort);
        ObjectMapper mapper = new ObjectMapper();   //map to json
        List<JSONObject> result = new ArrayList<>();

        List<String> type = new LinkedList<>();
            type.add("baidusousuo");

        try {
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                    new InetSocketTransportAddress(InetAddress.getByName(host),port));
            SearchResponse response = client.prepareSearch("crawler")
                    .setTypes(type.toArray(new String[type.size()]))
                    .setQuery(QueryBuilders.matchPhraseQuery("content", keyword))
                    .addSort("time.keyword", SortOrder.DESC)
                    .setSize(count).setFrom(0)
                    .get();
            for(SearchHit hit: response.getHits().getHits()) {
                Map<String, Object> source= hit.getSource();
                JSONObject tmpJson = new JSONObject(mapper.writeValueAsString(source));
                tmpJson.put("_id", hit.getId());
                tmpJson.put("keyword", keyword);
                tmpJson.put("contentType", "portal");
                result.add(tmpJson);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

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
            result.put("data",searchLast5FromBaiduSearch(keywordName, count));
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }

        return JSONResult.fillResultString(status,message,result);
    }
}
