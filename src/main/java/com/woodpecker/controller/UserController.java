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

import java.io.PrintWriter;

import com.woodpecker.domain.User;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String info, HttpServletResponse resp) throws Exception{

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
    }


    @RequestMapping(value = "/getKws", method = RequestMethod.POST)
    public void getKws(@RequestBody String info, HttpServletResponse resp) throws Exception{

        Map<String, Object> map = new HashMap<String, Object>();

        // 将info的格式由String转为jsonObject
        JSONObject jsonObject = new JSONObject(info);

        Integer id = (Integer) jsonObject.get("id");
        String token = (String) jsonObject.get("token");

        System.out.println(id + " try to get homepage data");

        // 验证token
        User user = JWT.unsign(token, User.class);

        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        if (user != null && userService.getUser(user).getId().equals(id)) {
            // 用户身份验证通过
            System.out.println("pass");

            List<String> list = new LinkedList<String>();
            list.add("成考"); list.add("作弊"); list.add("NJU");

            map.put("status", true);
            map.put("reason", "");
            map.put("keyword", list);

        } else {
            System.out.println("reject");
            map.put("status", false);
            map.put("reason", "用户身份验证失败");
            map.put("keyword", null);
        }

        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }

}
