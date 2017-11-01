package com.example.controller;

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

import com.example.domain.User;
import com.example.service.UserService;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    //添加一个日志器
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     *
     * @param info: 接受fetch传来的json数据
     * @param resp:
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String info, HttpServletResponse resp) throws Exception{

        // 将info的格式由String转为jsonObject
        JSONObject jsonObject = new JSONObject(info);

        String username = (String) jsonObject.get("username");
        String password = (String) jsonObject.get("password");

        System.out.println(username + " try to log in");

        // 验证用户身份
        User tmpUser = new User(username, password);



        String jsonStr = "";
        PrintWriter out = resp.getWriter();
        try {
        if (userService.login(tmpUser) != null) {
            System.out.println("pass");

            jsonStr = "{\"id\": 1, \"username\": \"r\", \"firstName\": \"r\", \"lastName\": \"r\", \"token\": \"fake-jwt-token\"}";
            out.write(jsonStr);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            System.out.println("reject");

            out.write(jsonStr);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }}
        catch (Exception e){
        System.out.println(e);}

    }
}
