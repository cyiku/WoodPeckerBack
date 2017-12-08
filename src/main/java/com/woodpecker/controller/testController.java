package com.woodpecker.controller;
import com.woodpecker.redis.RedisInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class testController {
    @Resource
    private RedisInterface redisInterface;

    @RequestMapping(value = "/testPage", method = RequestMethod.POST)
    String test() {
        String result = "Testpage";
        try {
            result = redisInterface.showAll();
            if(result.equals(""))result = "Nothing";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
