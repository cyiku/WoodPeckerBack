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

    @Resource
    private UserService userService;

    @RequestMapping(value = "/modifyPolarity", method = RequestMethod.POST)
    public String modifyPolarity(@RequestBody String info) {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);
            String source = (String) jsonObject.get("source");
            String id = (String) jsonObject.get("id");
            String polarity = (String) jsonObject.get("polarity");
            MsgPolarity msgPolarity = new MsgPolarity(source, id, polarity);
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());
            Integer ans = userService.addMsgPolarity(user, msgPolarity);
            System.out.println(ans);

        } catch (Exception e) {
            status = -1;
            message="未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }
}
