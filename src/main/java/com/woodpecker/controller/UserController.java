package com.woodpecker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.PrintWriter;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import com.woodpecker.domain.WeiboInfo;
import com.woodpecker.service.UserService;
import com.woodpecker.service.MongoService;

import com.woodpecker.utils.JWT;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private MongoService mongoService;

    //添加一个日志器
    private static final Logger debug_info = LoggerFactory.getLogger(UserController.class);
    private static final Logger server_info = LoggerFactory.getLogger(UserController.class);
    private static final Logger error_info = LoggerFactory.getLogger(UserController.class);

    //验证用户id与token是否对应
    public User verifyUser(Integer id, String token){
        System.out.println("Verifying user id = "+ id + " with token " + token);

        // 从token获取user信息
        User user = JWT.unsign(token, User.class);

        if (user != null && userService.getUser(user).getId().equals(id)) {
            // 用户身份验证通过
            System.out.println("Verification success.");
            return user;
        }
        else {
            // 验证不通过
            System.out.println("Verification failed.");
            return null;
        }
    }

    public void initializeLoggers(){
        System.out.println("Initializing loggers ...");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String info, HttpServletResponse resp){
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            System.out.println(username + " try to log in");

            // 验证用户身份
            User userFromInput = new User(username, password);
            User userFromDB = userService.getUser(userFromInput);

            // 要返回的json数据

            // 解决乱码
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();

            if (userFromDB != null) {
                // 用户身份验证通过
                System.out.println("pass");

                // 生成token
                String token = JWT.sign(userFromDB, 600L * 10L * 1000L);

                // 返回token
                map.put("id", userFromDB.getId());
                map.put("username", username);
                map.put("token", token);

            } else {
                System.out.println("reject");
                map.put("id", null);
                map.put("username", username);
                map.put("token", null);

            }
            JSONObject returnJson = new JSONObject(map);
            out.write(returnJson.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }



    @RequestMapping(value = "/getKws", method = RequestMethod.POST)
    public void getKeyword(@RequestBody String info, HttpServletResponse resp) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;

        try {

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");

            System.out.println(id + " try to get keyword");
            out = resp.getWriter();

            User user = verifyUser(id, token);
            if (null != user) {
                List<Keyword> keyword = userService.getKeyword(user);
                map.put("status", true);
                map.put("reason", "");
                map.put("keyword", keyword);
            } else {
                System.out.println("reject");
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
                map.put("keyword", null);
            }

        } catch (Exception e) {
            map.put("status", false);
            map.put("reason", "未知错误");
            map.put("keyword", null);
            System.out.println(e);
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }


    @RequestMapping(value = "/addKws", method = RequestMethod.POST)
    public void addKeyword(@RequestBody String info, HttpServletResponse resp) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String name = (String) jsonObject.get("name");
            JSONArray sites = (JSONArray) jsonObject.get("sites");

            System.out.println(id + " is adding keyword");
            out = resp.getWriter();

            User user = verifyUser(id, token);
            if(null!=user) {
                String sitesInDB = "";
                for(Integer i=0;i<sites.length();i++){
                    String site=sites.getString(i);
                    if(i==0)
                        sitesInDB = sitesInDB + site;
                    else
                        sitesInDB = sitesInDB + ";" + site;
                }
                System.out.println("Sites: "+sitesInDB);
                Keyword keyword = new Keyword(null,user.getId(),name,sitesInDB);
                List<Keyword> keywordForSearch = userService.searchKeyword(keyword);
                if(keywordForSearch.isEmpty()) {
                    System.out.println("Add result = " + userService.addKeyword(keyword));
                    keywordForSearch = userService.searchKeyword(keyword);
                    map.put("status", true);
                    map.put("reason", "");
                    map.put("keywordid", keywordForSearch.get(0).getKeywordid());
                    map.put("userid", keywordForSearch.get(0).getUserid());
                }
                else {
                    map.put("status",false);
                    map.put("reason", "关键词已存在");
                    map.put("keywordid", null);
                    map.put("userid",null);
                    System.out.println("key err");
                }
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
            }

        } catch (Exception e) {
            map.put("status",false);
            map.put("reason", "未知错误");
            System.out.println(e);
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }

    @RequestMapping(value = "/delKws", method = RequestMethod.POST)
    public void delKeyword(@RequestBody String info, HttpServletResponse resp) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String name = (String) jsonObject.get("name");

            System.out.println(id + " is deleting keyword, name = " + name);
            out = resp.getWriter();

            User user = verifyUser(id, token);
            if(null!=user) {
                Keyword keyword = new Keyword(null,user.getId(),name,null);
                List<Keyword> keywordForSearch = userService.searchKeyword(keyword);
                if(!keywordForSearch.isEmpty()) {
                    System.out.println("Del result = " + userService.delKeyword(keyword));
                    map.put("status", true);
                    map.put("reason", "");
                }
                else {
                    map.put("status", false);
                    map.put("reason", "不存在该条目");
                }
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
            }

        } catch (Exception e) {
            map.put("status",false);
            map.put("reason", "未知错误");
            System.out.println(e);
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }

    @RequestMapping(value = "/updKws", method = RequestMethod.POST)
    public void updateKeyword(@RequestBody String info, HttpServletResponse resp) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            Integer keywordid = (Integer) jsonObject.get("keywordid");
            String token = (String) jsonObject.get("token");
            String name = (String) jsonObject.get("name");
            JSONArray sites = (JSONArray) jsonObject.get("sites");
            System.out.println(id + " is updating keyword");
            System.out.println(keywordid + " " + name);
            out = resp.getWriter();

            User user = verifyUser(id, token);
            if(null!=user) {
                String sitesInDB = "";
                for(Integer i=0;i<sites.length();i++){
                    String site=sites.getString(i);
                    if(i==0)
                        sitesInDB = sitesInDB + site;
                    else
                        sitesInDB = sitesInDB + ";" + site;
                }
                System.out.println("Sites: "+sitesInDB);
                Keyword keyword = new Keyword(keywordid,user.getId(),name,sitesInDB);
                System.out.println("upd result = " + userService.updateKeyword(keyword));
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
            }
        } catch (Exception e) {
            map.put("status",false);
            map.put("reason", "未知错误");
            System.out.println(e);
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }

    @RequestMapping(value = "/testPage", method = RequestMethod.POST)
    public void testFunc(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("testFunc: " + info);
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        String outstr = "failed";
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            out = resp.getWriter();
            System.out.println("check1");
            WeiboInfo weiboInfo = mongoService.findModel("4170677152095257");
            System.out.println("check2");
            map.put("result",weiboInfo);
            JSONObject returnJSON = new JSONObject(map);
            outstr=returnJSON.toString();
        } catch(Exception e) {
            e.printStackTrace();
            outstr = e.toString();
        }
        out.write(outstr);
    }

}
