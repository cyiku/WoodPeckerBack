package com.example.demo.controller;

import com.example.demo.domain.Keyword;
import com.example.demo.domain.User;
import com.example.demo.security.JwtUser;
import com.example.demo.service.UserService;
import com.example.demo.util.JSONResult;
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

import static com.example.demo.util.GetUser.getPrincipal;

@RestController
@PreAuthorize("hasRole('USER')")
public class KeywordController {

    @Resource
    private UserService userService;


    //region Keywords
    @RequestMapping(value = "/getKws", method = RequestMethod.POST)
    public String getKeyword() {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {

            JwtUser jwtUser = getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            List<Keyword> keyword = userService.getKeyword(user);
            result.put("keyword", keyword);
        } catch (Exception e) {
            status = -1;
            message="未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }


    @RequestMapping(value = "/addKws", method = RequestMethod.POST)
    public String addKeyword(@RequestBody String info) {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String name = (String) jsonObject.get("name");
            JSONArray sites = (JSONArray) jsonObject.get("sites");


            JwtUser jwtUser = getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            String sitesInDB = "";
            for(Integer i=0;i<sites.length();i++){
                String site=sites.getString(i);
                if(i==0)
                    sitesInDB = sitesInDB + site;
                else
                    sitesInDB = sitesInDB + ";" + site;
            }
            System.out.println("Sites: "+sitesInDB);
            Keyword keyword = new Keyword(null,name,sitesInDB);
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            if(keywordForSearch.isEmpty()) {
                System.out.println("Add result = " + userService.addKeyword(user,keyword));
                keywordForSearch = userService.searchKeyword(user,keyword);
                result.put("keywordid", keywordForSearch.get(0).getKeywordid());
            }
            else {
                System.out.println("keyword exists");
                status = 0;
                message = "关键词已存在";
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/delKws", method = RequestMethod.POST)
    public String delKeyword(@RequestBody String info, HttpServletResponse resp) {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String name = (String) jsonObject.get("name");

            JwtUser jwtUser = getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            Keyword keyword = new Keyword(null,name,null);
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            if(!keywordForSearch.isEmpty()) {
                System.out.println("Del result = " + userService.delKeyword(user,keyword));
            }
            else {
                status=0;
                message="不存在该条目";
            }

        } catch (Exception e) {
            status=-1;
            message="未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/updKws", method = RequestMethod.POST)
    public String updateKeyword(@RequestBody String info, HttpServletResponse resp) {

        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);


            Integer keywordid = (Integer) jsonObject.get("keywordid");
            String name = (String) jsonObject.get("name");
            JSONArray sites = (JSONArray) jsonObject.get("sites");

            JwtUser jwtUser = getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            String sitesInDB = "";
            for(Integer i=0;i<sites.length();i++){
                String site=sites.getString(i);
                if(i==0)
                    sitesInDB = sitesInDB + site;
                else
                    sitesInDB = sitesInDB + ";" + site;
            }
            System.out.println("Sites: "+sitesInDB);
            Keyword keyword = new Keyword(keywordid,name,sitesInDB);
            List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
            Boolean canUpdate;
            if(keywordForSearch.size()==0)canUpdate=true;
            else if(keywordForSearch.get(0).getKeywordid().equals(keywordid))canUpdate=true;
            else canUpdate=false;
            if(canUpdate) {
                System.out.println("upd result = " + userService.updateKeyword(user, keyword));
            }
            else {
                status = 0;
                message = "关键字已存在";
            }

        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            System.out.println(e);
        }
        return JSONResult.fillResultString(status, message, result);
    }
    //endregion

}
