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

        // 要返回的json数据
        String jsonStr = "";
        PrintWriter out = resp.getWriter();
        if (userService.login(tmpUser) != null) {
            // 用户身份验证通过
            System.out.println("pass");

            // 生成token
            String token = JWT.sign(tmpUser, 10L * 1000L);

            // 返回token
            jsonStr = "{\"id\": 0, \"username\": \"" + username + "\", \"token\" : \"" + token + "\"}";
            out.write(jsonStr);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            System.out.println("reject");

            // 验证失败，返回空
            out.write(jsonStr);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
