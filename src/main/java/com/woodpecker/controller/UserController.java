package com.woodpecker.controller;

import com.woodpecker.domain.*;
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

import com.woodpecker.service.UserService;
import com.woodpecker.service.MongoService;

import com.woodpecker.utils.JWT;

import java.util.*;

@Controller
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private MongoService mongoService;

    //region Basic
    //添加一个日志器
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
    //endregion

    //region Keywords
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
                map.put("logout", true);
                map.put("reason", "用户身份验证失败");
            }

        } catch (Exception e) {
            map.put("status", false);
            map.put("logout", true);
            map.put("reason", "未知错误");
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
                Keyword keyword = new Keyword(null,name,sitesInDB);
                List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
                if(keywordForSearch.isEmpty()) {
                    System.out.println("Add result = " + userService.addKeyword(user,keyword));
                    keywordForSearch = userService.searchKeyword(user,keyword);
                    map.put("status", true);
                    map.put("reason", "");
                    map.put("keywordid", keywordForSearch.get(0).getKeywordid());
                }
                else {
                    System.out.println("keyword exists");
                    map.put("status",false);
                    map.put("reason", "关键词已存在");
                }
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("logout", true);
                map.put("reason", "用户身份验证失败");
            }

        } catch (Exception e) {
            map.put("status", false);
            map.put("logout", true);
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
                Keyword keyword = new Keyword(null,name,null);
                List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
                if(!keywordForSearch.isEmpty()) {
                    System.out.println("Del result = " + userService.delKeyword(user,keyword));
                    map.put("status", true);
                    map.put("reason", "");
                }
                else {
                    map.put("status", false);
                    map.put("logout", false);
                    map.put("reason", "不存在该条目");
                }
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("logout", true);
                map.put("reason", "用户身份验证失败");
            }

        } catch (Exception e) {
            map.put("status", false);
            map.put("logout", true);
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
                Keyword keyword = new Keyword(keywordid,name,sitesInDB);
                List<Keyword> keywordForSearch = userService.searchKeyword(user,keyword);
                Boolean canUpdate;
                if(keywordForSearch.size()==0)canUpdate=true;
                else if(keywordForSearch.get(0).getKeywordid()==keywordid)canUpdate=true;
                else canUpdate=false;
                if(canUpdate) {
                    System.out.println("upd result = " + userService.updateKeyword(user, keyword));
                    map.put("status", true);
                    map.put("reason", "");
                }
                else {
                    map.put("status",false);
                    map.put("logout",false);
                    map.put("reason", "关键字已存在");
                }
            }
            else {
                System.out.println("reject");
                map.put("status", false);
                map.put("logout", true);
                map.put("reason", "用户身份验证失败");
            }
        } catch (Exception e) {
            map.put("status",false);
            map.put("logout", true);
            map.put("reason", "未知错误");
            e.printStackTrace();
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
    }
    //endregion

    //region Collection
    @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
    public void addCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Add collection");
        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer userid = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String type = (String) jsonObject.get("type");
            JSONArray data =(JSONArray) jsonObject.get("data");
            String dataid;
            String data_str;
            System.out.println(data.toString());
            out = resp.getWriter();

            Date date = new Date(); //获取时间戳

            User user=verifyUser(userid,token);
            if(null!=user) {
                if(type=="table") {
                    List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                    for(Object o: data) {
                        dataid = String.valueOf(((JSONObject)o).get("id"));
                        data_str = ((JSONObject)o).toString();
                        tableCollections.add(new TableCollection(dataid,data_str,date.getTime(),1));
                    }
                    List<TableCollection> tableList = userService.searchTableCollection(user,tableCollections);
                    if(tableList.isEmpty()) {
                        userService.addTableCollection(user,tableCollections);
                    }
                    else {
                        userService.resetTableCollection(user,tableList);
                    }
                }
                else {
                    dataid = String.valueOf(data.getJSONObject(0).get("id"));
                    data_str = data.getJSONObject(0).toString();
                    NormalCollection normalCollection = new NormalCollection(dataid, data_str, 1);
                    List<NormalCollection> normalCollectionList;
                    switch(type) {
                        case "agency":
                            normalCollectionList = userService.searchAgencyCollection(user, normalCollection);
                            if (normalCollectionList.isEmpty()) {
                                userService.addAgencyCollection(user, normalCollection);
                            } else {
                                normalCollection = normalCollectionList.get(0);
                                userService.resetAgencyCollection(user, normalCollection);
                            }
                            break;
                        case "chart":
                            normalCollectionList = userService.searchChartCollection(user, normalCollection);
                            if (normalCollectionList.isEmpty()) {
                                userService.addChartCollection(user, normalCollection);
                            } else {
                                normalCollection = normalCollectionList.get(0);
                                userService.resetChartCollection(user, normalCollection);
                            }
                            break;
                        case "forum":
                            normalCollectionList = userService.searchForumCollection(user, normalCollection);
                            if (normalCollectionList.isEmpty()) {
                                userService.addForumCollection(user, normalCollection);
                            } else {
                                normalCollection = normalCollectionList.get(0);
                                userService.resetForumCollection(user, normalCollection);
                            }
                            break;
                        case "portal":
                            normalCollectionList = userService.searchPortalCollection(user, normalCollection);
                            if (normalCollectionList.isEmpty()) {
                                userService.addPortalCollection(user, normalCollection);
                            } else {
                                normalCollection = normalCollectionList.get(0);
                                userService.resetPortalCollection(user, normalCollection);
                            }
                            break;
                        case "weibo":
                            normalCollectionList = userService.searchWeiboCollection(user, normalCollection);
                            if (normalCollectionList.isEmpty()) {
                                userService.addWeiboCollection(user, normalCollection);
                            } else {
                                normalCollection = normalCollectionList.get(0);
                                userService.resetWeiboCollection(user, normalCollection);
                            }
                            break;
                        default:
                            System.out.println("default");
                            break;
                    }
                }
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
                map.put("logout", true);
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("reason", "未知错误");
            map.put("logout", true);
            e.printStackTrace();
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
        System.out.println(returnJson.toString());
    }
    @RequestMapping(value = "/delCollection", method = RequestMethod.POST)
    public void delCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Del collection");
        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer userid = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String type = (String) jsonObject.get("type");
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;
            out = resp.getWriter();

            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<JSONObject> result;

            User user=verifyUser(userid,token);
            if(null!=user) {
                if(type=="table") {
                    List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                    for(Object o: dataidList) {
                        dataid = String.valueOf(o);
                        tableCollections.add(new TableCollection(dataid,null,null,null));
                    }
                    userService.delTableCollection(user,tableCollections);
                    List<TableCollection> tableList = userService.getTableCollection(user);
                    Long tableid = null;
                    List<List> resultList = new ArrayList<List>();
                    result = null;
                    for(TableCollection table:tableList) {
                        if(null == tableid) {
                            tableid = table.getTableid();
                            result = new ArrayList<JSONObject>();
                            result.add(new JSONObject(table.getData()));
                        }
                        else if(tableid.equals(table.getTableid())) {
                            result.add(new JSONObject(table.getData()));
                        }
                        else {
                            tableid = table.getTableid();
                            resultList.add(result);
                            result = new ArrayList<JSONObject>();
                            result.add(new JSONObject(table.getData()));
                        }
                    }
                    if(null!=result)resultList.add(result);
                    map.put("collection", resultList);
                }
                else {
                    dataid = String.valueOf(dataidList.get(0));
                    NormalCollection normalCollection = new NormalCollection(dataid, null, null);
                    List<NormalCollection> normalCollectionList;
                    switch (type) {
                        case "agency":
                            userService.delAgencyCollection(user,normalCollection);
                            normalCollectionList = userService.getAgencyCollection(user);
                            break;
                        case "chart":
                            userService.delChartCollection(user,normalCollection);
                            normalCollectionList = userService.getChartCollection(user);
                            break;
                        case "forum":
                            userService.delForumCollection(user,normalCollection);
                            normalCollectionList = userService.getForumCollection(user);
                            break;
                        case "portal":
                            userService.delPortalCollection(user,normalCollection);
                            normalCollectionList = userService.getPortalCollection(user);
                            break;
                        case "weibo":
                            userService.delWeiboCollection(user,normalCollection);
                            normalCollectionList = userService.getWeiboCollection(user);
                            break;
                        default:
                            normalCollectionList = new ArrayList<>();
                            System.out.println("unknown type " + type);
                            break;
                    }
                    result = new ArrayList<JSONObject>();
                    for (NormalCollection normal : normalCollectionList) {
                        result.add(new JSONObject(normal.getData()));
                    }
                    map.put("collection",result);
                }
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
                map.put("logout", true);
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("reason", "未知错误");
            map.put("logout", true);
            e.printStackTrace();
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
        System.out.println(returnJson.toString());
    }
    @RequestMapping(value = "/getCollection", method = RequestMethod.POST)
    public void getCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Get collection");
        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer userid = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String type = (String) jsonObject.get("type");
            out = resp.getWriter();

            List<JSONObject> result;

            User user=verifyUser(userid,token);
            if(null!=user) {
                if(type=="table") {
                    List<TableCollection> tableList = userService.getTableCollection(user);
                    Long tableid = null;
                    List<List> resultList = new ArrayList<List>();
                    result = null;
                    for(TableCollection table:tableList) {
                        if(null == tableid) {
                            tableid = table.getTableid();
                            result = new ArrayList<JSONObject>();
                            result.add(new JSONObject(table.getData()));
                        }
                        else if(tableid.equals(table.getTableid())) {
                            result.add(new JSONObject(table.getData()));
                        }
                        else {
                            tableid = table.getTableid();
                            resultList.add(result);
                            result = new ArrayList<JSONObject>();
                            result.add(new JSONObject(table.getData()));
                        }
                    }
                    if(null!=result)resultList.add(result);
                    map.put("collection", resultList);
                }
                else {
                    List<NormalCollection> normalCollectionList;
                    switch(type) {
                        case "agency":
                            normalCollectionList = userService.getAgencyCollection(user);
                            break;
                        case "chart":
                            normalCollectionList = userService.getChartCollection(user);
                            break;
                        case "forum":
                            normalCollectionList = userService.getForumCollection(user);
                            break;
                        case "portal":
                            normalCollectionList = userService.getPortalCollection(user);
                            break;
                        case "weibo":
                            normalCollectionList = userService.getWeiboCollection(user);
                            break;
                        default:
                            normalCollectionList = new ArrayList<>();
                            System.out.println("default");
                    }
                    result = new ArrayList<JSONObject>();
                    for(NormalCollection normalCollection:normalCollectionList) {
                        result.add(new JSONObject(normalCollection.getData()));
                    }
                    map.put("collection",result);
                }
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
                map.put("collection", null);
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("reason", "未知错误");
            map.put("collection", null);
            e.printStackTrace();
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
        System.out.println(returnJson.toString());
    }

    @RequestMapping(value = "/isCollection", method = RequestMethod.POST)
    public void isCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Verify collection");
        Map<String, Object> map = new HashMap<String, Object>();
        // 解决乱码
        resp.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            Integer userid = (Integer) jsonObject.get("id");
            String token = (String) jsonObject.get("token");
            String type = (String) jsonObject.get("type");
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;
            out = resp.getWriter();

            User user=verifyUser(userid,token);
            if(null!=user) {
                NormalCollection normalCollection;
                List<NormalCollection> normalCollectionList;
                if(type=="table") {
                    List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                    for(Object o: dataidList) {
                        dataid = String.valueOf(o);
                        tableCollections.add(new TableCollection(dataid,null,null,null));
                    }
                    List<TableCollection> tableList = userService.searchTableCollection(user,tableCollections);
                    if(tableList.isEmpty()) {
                        map.put("iscollection",false);
                    }
                    else {
                        map.put("iscollection",true);
                    }
                }
                else {
                    dataid = String.valueOf(dataidList.get(0));
                    normalCollection = new NormalCollection(dataid,null,null);
                    switch(type) {
                        case "agency":
                            normalCollectionList = userService.searchAgencyCollection(user, normalCollection);
                            break;
                        case "chart":
                            normalCollectionList = userService.searchChartCollection(user, normalCollection);
                            break;
                        case "forum":
                            normalCollectionList = userService.searchForumCollection(user, normalCollection);
                            break;
                        case "portal":
                            normalCollectionList = userService.searchPortalCollection(user, normalCollection);
                            break;
                        case "weibo":
                            normalCollectionList = userService.searchWeiboCollection(user, normalCollection);
                            break;
                        default:
                            normalCollectionList = new ArrayList<>();
                            System.out.println("unknown type " + type);
                            break;
                    }
                    if(normalCollectionList.isEmpty()) {
                        map.put("iscollection",false);
                    }
                    else if(normalCollectionList.get(0).getIscollection()==0) {
                        map.put("iscollection",false);
                    }
                    else {
                        map.put("iscollection",true);
                    }
                }
                map.put("status", true);
                map.put("reason", "");
            }
            else {
                map.put("status", false);
                map.put("reason", "用户身份验证失败");
                map.put("logout", true);
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("reason", "未知错误");
            map.put("logout", true);
            e.printStackTrace();
        }
        JSONObject returnJson = new JSONObject(map);
        out.write(returnJson.toString());
        System.out.println(returnJson.toString());
    }

    //endregion

    //region test
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
            //test code
            //userService.newUser(info);
            logger.info("!!");
            //test code
            map.put("result","success");
            JSONObject returnJSON = new JSONObject(map);
            outstr=returnJSON.toString();
        } catch(Exception e) {
            e.printStackTrace();
            outstr = e.toString();
        }
        out.write(outstr);
    }
    //endregion
}
