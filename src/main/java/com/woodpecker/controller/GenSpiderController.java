package com.woodpecker.controller;

import com.woodpecker.util.JSONResult;
import com.woodpecker.util.GenSpider;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GenSpiderController {

    private String [] jsonArrayToStringArray (JSONArray jsonArray) {
        String[] stringArray = new String[jsonArray.length()];
        for(int i = 0, count = jsonArray.length(); i< count; i++) {
            stringArray[i] = jsonArray.getString(i);
        }
        return stringArray;
    }
    @RequestMapping(value = "/genSpider", method = RequestMethod.POST)
    public String genSpider(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            JSONObject jsonObject = new JSONObject(info);

            String dbName = (String) jsonObject.get("dbName");
            // String mongoCollection = (String) jsonObject.get("mongoCollection");
            String mongoHost = (String) jsonObject.get("mongoHost");
            String mongoPort = Integer.toString((int)jsonObject.get("mongoPort"));
            String projectName = (String) jsonObject.get("projectName");
            String spiderName = (String) jsonObject.get("spiderName");
            // String spiderType = (String) jsonObject.get("spiderType");
            String itemRule = (String) jsonObject.get("itemRule");

            String[] startURL = jsonArrayToStringArray((JSONArray) jsonObject.get("startURL"));
            String[] allowedDomains = jsonArrayToStringArray((JSONArray) jsonObject.get("allowedDomains"));
            // String[] defaultValue = jsonArrayToStringArray((JSONArray) jsonObject.get("default"));
            String[] property = jsonArrayToStringArray((JSONArray) jsonObject.get("property"));
            String[] rule = jsonArrayToStringArray((JSONArray) jsonObject.get("rule"));
            String downloadDelay = Integer.toString((int)jsonObject.get("downloadDelay"));

            GenSpider genSpider = new GenSpider("src/main/java/com/woodpecker/templates");

            // First Step: 生成工程目录
            String projectDir = projectName + "/" + projectName + "/" + "spiders";
            genSpider.genDir(projectDir);

            // Second Step: 根据项目名生成一些简单的模板
            genSpider.genTmp(projectName);

            // Third Step: 根据mongo生成settings
            Map<String, Object> mongoProp = new HashMap<>();
            mongoProp.put("MONGO_HOST", mongoHost);
            mongoProp.put("MONGO_PORT", mongoPort);
            mongoProp.put("MONGODB_DBNAME", dbName);
            mongoProp.put("MONGO_COLLECTION", projectName);
            genSpider.genSettings(projectName, downloadDelay, mongoProp);

            // Fourth Step: 根据类型生成相应的item
            genSpider.genItem(projectName, property);

            // Fifth Step: 生成具体的spider
            genSpider.genSpider(projectName, spiderName, allowedDomains, startURL, itemRule, property, rule);

            // Sixth Step: 生成具体的pipeline
            genSpider.genPipelines(projectName, property);
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }
}
