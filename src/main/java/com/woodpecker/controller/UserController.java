package com.woodpecker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;

import java.awt.print.PrinterGraphics;
import java.io.PrintWriter;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import com.woodpecker.service.UserService;

import com.woodpecker.utils.JWT;

import java.util.HashMap;
import java.util.Map;

import java.util.LinkedList;
import java.util.List;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    //添加一个日志器
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //验证用户id与token是否对应
    private User verifyUser(Integer id, String token){
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
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");

            System.out.println(id + " try to get keyword");

            // 解决乱码
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();

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

            JSONObject returnJson = new JSONObject(map);
            out.write(returnJson.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @RequestMapping(value = "/addKws", method = RequestMethod.POST)
    public void addKeyword(@RequestBody String info, HttpServletResponse resp) throws Exception{

        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer id = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String name = (String) jsonObject.get("name");
            JSONArray sites = (JSONArray) jsonObject.get("sites");

            System.out.println(id + "is adding keyword");


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
                Keyword keyword = new Keyword(user.getId(),name,sitesInDB);
                userService.addKeyword(keyword);
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
            }

        } catch (DuplicateKeyException e) {
            map.put("status",false);
            map.put("reason", "关键词已存在");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }

}
