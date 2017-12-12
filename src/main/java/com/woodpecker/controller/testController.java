package com.woodpecker.controller;
import com.woodpecker.redis.RedisInterface;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class testController {
    @Resource
    private RedisInterface redisInterface;

    @RequestMapping(value = "/testPage", method = RequestMethod.POST)
    String test() {
        Map<String, Object> test = new HashMap<>();
        String result = "Testpage";
        try {
            test.put("1","\"test2");
            JSONObject jsonObject = new JSONObject(test);
            System.out.println(jsonObject.toString());
            if(result.equals(""))result = "Nothing";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
