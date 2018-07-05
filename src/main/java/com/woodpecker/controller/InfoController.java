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
    /**
     * 使用es为关键字提取指定来源，指定页数的消息
     */
    @Resource
    UserService userService;

    @Value("${spring.es.host}")
    private String esHost;

    @Value("${spring.es.port}")
    private String esPort;

    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public String getInfo(@RequestBody String info) {

        // status: 状态码，message: 存储错误信息
        Integer status = 1;
        String message = "";

        // 获取用户，为以后查询用户信息做准备
        JwtUser jwtUser = GetUser.getPrincipal();
        User user = userService.findByUserName(jwtUser.getUsername());

        // 获取request body中存储的信息：关键字，第几页，来源
        JSONObject jsonObject = new JSONObject(info);
        String keywordName = (String)jsonObject.get("keyword");
        Integer page = (Integer) jsonObject.get("page");
        String webType = (String) jsonObject.get("type");

        // 每页的数目，这里count=10，若page=1，则返回第0-9条。
        int count = 10;

        // 这里把培训机构这一大类的每页数量改成5，因为是培训机构每条太长了。
        if (webType.equals("agency")) {
            count = 5;
        }

        // 根据请求的页数和每页的数目，可以得到开始的下标，方便es查询。
        int beginIndex = (page - 1) * count;

        // 获取所有的网站，Site包括: type(四大类：论坛，门户...)，name（名称：新浪微博...），tableName(在mongo中的名称，es的索引)
        List<Site> sites = userService.getSite();
        
        // 根据指定的类型，得到所有的tableName，为以后es查询做准备
        List<String> tableNames = new LinkedList<>();
        if (webType.equals("forum")) {
            for(Site site: sites){
                if (site.getType().equals("论坛")){
                    tableNames.add(site.getTableName());
                }
            }    
        } else if (webType.equals("portal")) {
            for(Site site: sites){
                if (site.getType().equals("门户网站")){
                    tableNames.add(site.getTableName());
                }
            }
        } else if (webType.equals("agency")) {
            for(Site site: sites){
                if (site.getType().equals("培训机构")){
                    tableNames.add(site.getTableName());
                }
            }
        } else if (webType.equals("weibo")) {
            tableNames.add("weibo");
        } else if (webType.equals("business")) {
            for(Site site: sites){
                if (site.getType().equals("商务资讯")){
                    tableNames.add(site.getTableName());
                }
            }
        } else if (webType.equals("industry")) {
            for(Site site: sites){
                if (site.getType().equals("行业动态")){
                    tableNames.add(site.getTableName());
                }
            }
        }
        
        // 使用es查到查询结果
        List<JSONObject> result = EsSearch.esSearch(esHost, esPort, beginIndex, count, keywordName, tableNames);

        // 得到查询结果后，逐条遍历每条结果，若该用户修改过该结果的情感极性，则把该结果的情感极性替换成用户修改过的。
        // 先获取用户修改过情感极性的消息
        List<MsgPolarity> modifyPolarity = userService.getModifyPolarity(user);

        // 遍历查询结果
        for(int i = 0; i < result.size(); ++i) {
            // 获得第i个结果的id，查询是否在modifyPolarity里
            String oneResultId = (String)result.get(i).get("_id"); 
            for (int j = 0; j < modifyPolarity.size(); ++j) {
                if (oneResultId.equals(modifyPolarity.get(j).getId())) {
                    // 若在里面则替换情感极性
                    result.get(i).put("sentiment", modifyPolarity.get(j).getPolarity());
                }
            }
        }

        // 返回result
        return JSONResult.fillResultString(status,message,result);
    }

}
