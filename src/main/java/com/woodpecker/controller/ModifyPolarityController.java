package com.woodpecker.controller;

import com.woodpecker.domain.User;
import com.woodpecker.domain.MsgPolarity;
import com.woodpecker.security.JwtUser;
import com.woodpecker.service.UserService;
import com.woodpecker.util.GetUser;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@PreAuthorize("hasRole('USER')")
public class ModifyPolarityController {
    /**
     * 修改消息的情感极性接口
     */

    @Resource
    private UserService userService;

    @RequestMapping(value = "/modifyPolarity", method = RequestMethod.POST)
    public String modifyPolarity(@RequestBody String info) {

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // source: 消息的来源，小来源，不是四大类
            // id：消息的id
            // polarity：修改后的情感极性
            String source = (String) jsonObject.get("source");
            String id = (String) jsonObject.get("id");
            String polarity = (String) jsonObject.get("polarity");
            MsgPolarity msgPolarity = new MsgPolarity(source, id, polarity);

            // 获取用户，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 插入到MsgPolarity表里
            userService.addMsgPolarity(user, msgPolarity);
        } catch (Exception e) {
            status = -1;
            message="未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }
}
