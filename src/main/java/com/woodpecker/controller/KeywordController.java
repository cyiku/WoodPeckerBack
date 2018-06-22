package com.woodpecker.controller;

import com.woodpecker.domain.Keyword;
import com.woodpecker.domain.User;
import com.woodpecker.security.JwtUser;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import com.woodpecker.util.GetUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('USER')")
public class KeywordController {
    /**
     * 关键字的Controller
     */

    @Resource
    private UserService userService;

    @RequestMapping(value = "/getKws", method = RequestMethod.POST)
    public String getKeyword() {

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());
            
            // 获取keyword
            List<Keyword> keyword = userService.getKeyword(user);
            result.put("keyword", keyword);
        } catch (Exception e) {
            status = -1;
            message="未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }


    @RequestMapping(value = "/addKws", method = RequestMethod.POST)
    public String addKeyword(@RequestBody String info) {

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // 获取要添加的关键字的名称
            String name = (String) jsonObject.get("name");
            if(name.equals("")) {
                // 关键字为空
                status = 0;
                message = "关键字为空";
                return JSONResult.fillResultString(status,message,result);
            } else if (name.length() > 5) {
                // 关键字太长
                status = 0;
                message = "关键字太长";
                return JSONResult.fillResultString(status,message,result);
            }
            
            // 获取要添加的关键字的站点
            JSONArray sites = (JSONArray) jsonObject.get("sites");
            if(sites.length()==0) {
                // 站点为空
                status = 0;
                message = "爬取站点为空";
                return JSONResult.fillResultString(status,message,result);
            }

            // 将sites转为string
            String sitesInDB = "";
            for(Integer i=0;i<sites.length();i++){
                String site=sites.getString(i);
                if(i==0)
                    sitesInDB = sitesInDB + site;
                else
                    sitesInDB = sitesInDB + ";" + site;
            }
            

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 查看该关键字是否存在
            Keyword keyword = new Keyword(null,name,sitesInDB);
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            
            // 如果不存在
            if(keywordForSearch.isEmpty()) {
                // 插入该关键字
                userService.addKeyword(user,keyword);
                // 返回该关键字的id
                keywordForSearch = userService.searchKeyword(user,keyword);
                result.put("keywordid", keywordForSearch.get(0).getKeywordid());
            }
            else {
                // 如果存在
                System.out.println("keyword exists");
                status = 0;
                message = "关键词已存在";
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/delKws", method = RequestMethod.POST)
    public String delKeyword(@RequestBody String info, HttpServletResponse resp) {

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // 获取要添加的关键字的名称
            String name = (String) jsonObject.get("name");

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 查询该关键字用户是否关注
            Keyword keyword = new Keyword(null,name,null);
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            // 如果关注则删除
            if(!keywordForSearch.isEmpty()) {
                userService.delKeyword(user,keyword);
            }
            else {
                // 没关注
                status=0;
                message="不存在该条目";
            }

        } catch (Exception e) {
            status=-1;
            message="未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/updKws", method = RequestMethod.POST)
    public String updateKeyword(@RequestBody String info, HttpServletResponse resp) {

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // 获取修改后的关键字名称
            String name = (String) jsonObject.get("name");
            if(name.equals("")) {
                status = 0;
                message = "关键字为空";
                return JSONResult.fillResultString(status,message,result);
            } else if (name.length() > 5) {
                // 关键字太长
                status = 0;
                message = "关键字太长";
                return JSONResult.fillResultString(status,message,result);
            }

            // 获取要添加的关键字的站点
            JSONArray sites = (JSONArray) jsonObject.get("sites");
            if(sites.length()==0) {
                status = 0;
                message = "爬取站点为空";
                return JSONResult.fillResultString(status,message,result);
            }

            // 将sites转为string
            String sitesInDB = "";
            for(Integer i=0;i<sites.length();i++){
                String site=sites.getString(i);
                if(i==0)
                    sitesInDB = sitesInDB + site;
                else
                    sitesInDB = sitesInDB + ";" + site;
            }

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 获取Keyword id
            Integer keywordid = (Integer) jsonObject.get("keywordid");
            Keyword keyword = new Keyword(keywordid,name,sitesInDB);

            // 查询要更新的关键字是否已经存在
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            
            Boolean canUpdate;
            // 没查询到相关名字的关键字
            if(keywordForSearch.size()==0)
                canUpdate=true;
            else if(keywordForSearch.get(0).getKeywordid().equals(keywordid)) {
                // 查询到相关名字的关键字了，考虑到可能是更新站点的，所以也可以更新
                canUpdate=true;
            }
            else 
                canUpdate=false;
            
            if(canUpdate) {
                // 更新该关键字
                userService.updateKeyword(user, keyword);
            }
            else {
                status = 0;
                message = "关键字已存在";
            }

        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

}
