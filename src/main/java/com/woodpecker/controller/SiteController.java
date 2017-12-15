package com.woodpecker.controller;

import com.woodpecker.domain.Site;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('USER')")
public class SiteController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/getSites", method = RequestMethod.POST)
    public String getSites() {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            List<Site> sites = userService.getSite();

            List<String> portal = new LinkedList<>();   //门户
            List<String> forum = new LinkedList<>();
            List<String> weibo = new LinkedList<>();
            List<String> agency = new LinkedList<>();
            for (Site site: sites) {
                String type = site.getType();
                String name = site.getName();
                switch (type) {
                    case "论坛":
                        forum.add(name);
                        break;
                    case "门户网站":
                        portal.add(name);
                        break;
                    case "微博":
                        weibo.add(name);
                        break;
                    case "培训机构":
                        agency.add(name);
                        break;
                }
            }
            result.put("forum", forum);
            result.put("portal", portal);
            result.put("weibo", weibo);
            result.put("agency", agency);

        } catch (Exception e) {
            status = -1;
            message="未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }

}
