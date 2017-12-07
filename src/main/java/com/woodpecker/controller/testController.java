package com.woodpecker.controller;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
public class testController {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Value("${spring.redis.host}")
    private String host;

    @RequestMapping(value = "/testPage", method = RequestMethod.POST)
    String test() {
        System.out.println(redisTemplate.toString());
        redisTemplate.setEnableTransactionSupport(true);
        try {
//            redisTemplate.opsForValue().set("name", "张三");
            Set<Object> keys = redisTemplate.keys("*");
            for(Object o:keys) {
                Object val = redisTemplate.opsForValue().get(o);
                System.out.println(o + " " + val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Testpage";
    }
}
