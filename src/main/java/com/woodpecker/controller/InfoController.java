package com.woodpecker.controller;

import com.woodpecker.domain.User;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('USER')")
public class InfoController {
    @Resource
    UserService userService;

    @RequestMapping(value = "/getWeibo", method = RequestMethod.POST)
    public String getWeibo(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        List<JSONObject> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("keyword");
            if (null != userService.existsTable(keywordName + "_weibo")) {
                List<String> strings = userService.getInfo(keywordName,"weibo");
                for(String str:strings) {
                    result.add(new JSONObject(str));
                }
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getAgency", method = RequestMethod.POST)
    public String getAgency(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        List<JSONObject> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("keyword");
            if (null != userService.existsTable(keywordName + "_agency")) {
                List<String> strings = userService.getInfo(keywordName,"agency");
                for(String str:strings) {
                    result.add(new JSONObject(str));
                }
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getPortal", method = RequestMethod.POST)
    public String getPortal(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        List<JSONObject> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("keyword");
            if (null != userService.existsTable(keywordName + "_portal")) {
                List<String> strings = userService.getInfo(keywordName,"portal");
                for(String str:strings) {
                    result.add(new JSONObject(str));
                }
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status,message,result);
    }

    @RequestMapping(value = "/getForum", method = RequestMethod.POST)
    public String getForum(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        List<JSONObject> result = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String)jsonObject.get("keyword");
            if (null != userService.existsTable(keywordName + "_forum")) {
                List<String> strings = userService.getInfo(keywordName,"forum");
                for(String str:strings) {
                    result.add(new JSONObject(str));
                }
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status,message,result);
    }
}
