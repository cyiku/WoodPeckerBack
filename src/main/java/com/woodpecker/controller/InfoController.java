package com.woodpecker.controller;

import com.woodpecker.domain.Site;
import com.woodpecker.domain.User;
import com.woodpecker.domain.MsgPolarity;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import com.woodpecker.util.EsSearch;
import com.woodpecker.util.GetUser;
import com.woodpecker.security.JwtUser;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class InfoController {
    @Resource
    UserService userService;

    @Value("${spring.es.host}")
    private String esHost;

    @Value("${spring.es.port}")
    private String esPort;

    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public String getInfo(@RequestBody String info) {
        Integer status = 1;
        String message = "";

        JwtUser jwtUser = GetUser.getPrincipal();
        User user = userService.findByUserName(jwtUser.getUsername());

        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        Integer page = (Integer) jsonObject.get("page");
        String webType = (String) jsonObject.get("type");

        int count = 10;

        List<Site> sites = userService.getSite();
        List<String> type = new LinkedList<>();
        if (webType.equals("forum")) {
            //System.out.println("In forum");
            for(Site site: sites){
                if (site.getType().equals("论坛")){
                    type.add(site.getTableName());
                }
            }    
        } else if (webType.equals("portal")) {
            //System.out.println("In portal");
            for(Site site: sites){
                if (site.getType().equals("门户网站")){
                    type.add(site.getTableName());
                }
            }
        } else if (webType.equals("agency")) {
            //System.out.println("In agency");
            for(Site site: sites){
                if (site.getType().equals("培训机构")){
                    type.add(site.getTableName());
                }
            }
            count = 5;  // 培训机构每条消息太特么长了...
        } else if (webType.equals("weibo")) {
            //System.out.println("In weibo");
            type.add("weibo");
        }
        
        int beginIndex = (page - 1) * 10;
        List<JSONObject> result = EsSearch.esSearch(esHost, esPort, beginIndex, count, keywordName, type);
        List<MsgPolarity> modifyPolarity = userService.getModifyPolarity(user);
        for(int i = 0; i < result.size(); ++i) {
            String oneResultId = (String)result.get(i).get("_id"); 
            for (int j = 0; j < modifyPolarity.size(); ++j) {
                if (oneResultId.equals(modifyPolarity.get(j).getId())) {
                    //System.out.println("oneResultId: " + oneResultId);
                    //System.out.println("before modify: " + result.get(i).get("sentiment"));
                    result.get(i).put("sentiment", modifyPolarity.get(j).getPolarity());
                    //System.out.println("after modify: " + result.get(i).get("sentiment"));
                }
            }
        }
        return JSONResult.fillResultString(status,message,result);
    }

}
