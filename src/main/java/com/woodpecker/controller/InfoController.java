package com.woodpecker.controller;

import com.woodpecker.domain.Site;
import com.woodpecker.domain.User;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.InetAddress;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.databind.ObjectMapper;     //map to json

@RestController
@PreAuthorize("hasRole('USER')")
public class InfoController {
    @Resource
    UserService userService;

    @Value("${spring.es.host}")
    private String esHost;

    @Value("${spring.es.port}")
    private String esPort;

    private List<JSONObject> esSearch(String keyword, int count, List<String> type) {
        String host = esHost;
        int port = Integer.parseInt(esPort);
        ObjectMapper mapper = new ObjectMapper();   //map to json
        List<JSONObject> result = new ArrayList<>();

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
                if (!tmpJson.has("keyword")) {
                    tmpJson.put("keyword", keyword);
                }
                result.add(tmpJson);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/getWeibo", method = RequestMethod.POST)
    public String getWeibo(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        int count = 50;
        if (jsonObject.has("search")) {
            count = 20;
        }
//        try {
//            JSONObject jsonObject = new JSONObject(info);
//            String keywordName = (String)jsonObject.get("keyword");
//            if (null != userService.existsTable(keywordName + "_weibo")) {
//                List<String> strings = userService.getInfo(keywordName,"weibo");
//                for(String str:strings) {
//                    result.add(new JSONObject(str));
//                }
//            }
//        } catch (Exception e) {
//            status = -1;
//            message = "未知错误";
//            e.printStackTrace();
//        }

        List<String> type = new LinkedList<>();
        type.add("weibo");
        List<JSONObject> result = esSearch(keywordName, count, type);
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getAgency", method = RequestMethod.POST)
    public String getAgency(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        int count = 50;
        if (jsonObject.has("search")) {
            count = 20;
        }
//        try {
//            JSONObject jsonObject = new JSONObject(info);
//            String keywordName = (String)jsonObject.get("keyword");
//            if (null != userService.existsTable(keywordName + "_agency")) {
//                List<String> strings = userService.getInfo(keywordName,"agency");
//                for(String str:strings) {
//                    result.add(new JSONObject(str));
//                }
//            }
//        } catch (Exception e) {
//            status = -1;
//            message = "未知错误";
//            e.printStackTrace();
//        }
        List<Site> sites = userService.getSite();
        List<String> type = new LinkedList<>();
        for(Site site: sites){
            if (site.getType().equals("培训机构")){
                type.add(site.getTableName());
            }
        }
        List<JSONObject> result = esSearch(keywordName, count, type);
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getPortal", method = RequestMethod.POST)
    public String getPortal(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        int count = 50;
        if (jsonObject.has("search")) {
            count = 20;
        }
//        try {
//            JSONObject jsonObject = new JSONObject(info);
//            String keywordName = (String)jsonObject.get("keyword");
//            if (null != userService.existsTable(keywordName + "_portal")) {
//                List<String> strings = userService.getInfo(keywordName,"portal");
//                for(String str:strings) {
//                    result.add(new JSONObject(str));
//                }
//            }
//        } catch (Exception e) {
//            status = -1;
//            message = "未知错误";
//            e.printStackTrace();
//        }
        List<Site> sites = userService.getSite();
        List<String> type = new LinkedList<>();
        for(Site site: sites){
            if (site.getType().equals("门户网站")){
                type.add(site.getTableName());
            }
        }
        List<JSONObject> result = esSearch(keywordName, count, type);
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getForum", method = RequestMethod.POST)
    public String getForum(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        int count = 50;
        if (jsonObject.has("search")) {
            count = 20;
        }
//        try {
//            JSONObject jsonObject = new JSONObject(info);
//            String keywordName = (String)jsonObject.get("keyword");
//            if (null != userService.existsTable(keywordName + "_forum")) {
//                List<String> strings = userService.getInfo(keywordName,"forum");
//                for(String str:strings) {
//                    result.add(new JSONObject(str));
//                }
//            }
//        } catch (Exception e) {
//            status = -1;
//            message = "未知错误";
//            e.printStackTrace();
//        }
        List<Site> sites = userService.getSite();
        List<String> type = new LinkedList<>();
        for(Site site: sites){
            if (site.getType().equals("论坛")){
                type.add(site.getTableName());
            }
        }
        List<JSONObject> result = esSearch(keywordName, count, type);
        return JSONResult.fillResultString(status,message,result);
    }
}
