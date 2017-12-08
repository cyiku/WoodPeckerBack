package com.woodpecker.controller;
import com.woodpecker.redis.RedisInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RestController
public class testController {
    @Resource
    private RedisInterface redisInterface;

    @RequestMapping(value = "/testPage", method = RequestMethod.POST)
    String test() {
        try {
            redisInterface.showAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Testpage";
    }
}
