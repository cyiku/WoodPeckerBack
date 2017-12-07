package com.woodpecker.controller;

import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class StatsController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/getDataSourceNum", method = RequestMethod.POST)
    public String getDataSrcNum(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> appendList = Arrays.asList("forum","weibo","portal","agency");

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            Map<String, Object> numList = new HashMap<String, Object>();
            for(String append:appendList) {
                String tableName = keywordName + "_" + append;
                Integer count = 0;
                if(null != userService.existsTable(tableName)) {
                    count = userService.tableCount(tableName);
                };
                numList.put(append,count);
            }
            result.put("num",numList);
        } catch(Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }
}
