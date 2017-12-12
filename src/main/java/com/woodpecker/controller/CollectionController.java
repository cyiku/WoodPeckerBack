package com.woodpecker.controller;

import com.woodpecker.domain.NormalCollection;
import com.woodpecker.domain.TableCollection;
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
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class CollectionController {

    @Resource
    private UserService userService;

    //region Collection
    @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
    public String addCollection(@RequestBody String info) {
        System.out.println("Add collection");
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String type = (String) jsonObject.get("type");
            JSONArray data =(JSONArray) jsonObject.get("data");
            String dataid;
            String data_str;
            System.out.println(data.toString());

            Date date = new Date(); //获取时间戳

            if(type.equals("table")) {
                List<TableCollection> tableCollections = new ArrayList<>();
                for(int i=0;i<data.length();i++) {
                    JSONObject o=(JSONObject)data.get(i);
                    dataid = String.valueOf(o.get("id"));
                    System.out.println(o.toString());
                    data_str = o.toString().replaceAll("'","''").replaceAll("\\\\","\\\\\\\\");
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
                if(data.getJSONObject(0).has("_id")) {
                    dataid = String.valueOf(data.getJSONObject(0).get("_id"));
                } else {
                    dataid = String.valueOf(date.getTime()) + String.valueOf(data.getJSONObject(0).hashCode());
                }
                data_str = data.getJSONObject(0).toString().replaceAll("'","''").replaceAll("\\\\","\\\\\\\\");
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
                        //map.put("_id",dataid);
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
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, map);
    }

    @RequestMapping(value = "/delCollection", method = RequestMethod.POST)
    public String delCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Del collection");
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String type = (String) jsonObject.get("type");
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;

            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<JSONObject> result;

            if(type.equals("table")) {
                List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                for(int i=0;i<dataidList.length();i++) {
                    dataid=String.valueOf(dataidList.get(i));
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
                    System.out.println(normal.getData());
                    result.add(new JSONObject(normal.getData()));
                }
                map.put("collection",result);
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, map);
    }
    @RequestMapping(value = "/getCollection", method = RequestMethod.POST)
    public String getCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Get collection");
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<>();
        try {

            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String type = (String) jsonObject.get("type");

            List<JSONObject> result;

            if(type.equals("table")) {
                List<TableCollection> tableList = userService.getTableCollection(user);
                System.out.println(tableList.size());
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
                    System.out.println(normalCollection.getData());
                    result.add(new JSONObject(normalCollection.getData()));
                }
                map.put("collection",result);
            }
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, map);
    }

    @RequestMapping(value = "/isCollection", method = RequestMethod.POST)
    public String isCollection(@RequestBody String info, HttpServletResponse resp) {
        System.out.println("Verify collection");
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            String type = (String) jsonObject.get("type");
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;

            NormalCollection normalCollection;
            List<NormalCollection> normalCollectionList;
            if(type.equals("table")) {
                List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                for(int i=0;i<dataidList.length();i++) {
                    dataid = String.valueOf(dataidList.get(i));
                    tableCollections.add(new TableCollection(dataid,null,null,null));
                }
                List<TableCollection> tableList = userService.searchTableCollection(user,tableCollections);
                if(tableList.isEmpty()) {
                    map.put("iscollection",false);
                }
                else {
                    Boolean result = true;
                    for(TableCollection tableCollection: tableList) {
                        if(tableCollection.getIscollection() == 0) {
                            result=false;
                            break;
                        }
                    }
                    map.put("iscollection",result);
                }
            }
            else {
                dataid = String.valueOf(dataidList.get(0));
                normalCollection = new NormalCollection(dataid, null, null);
                switch (type) {
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
                if (normalCollectionList.isEmpty()) {
                    map.put("iscollection", false);
                } else if (normalCollectionList.get(0).getIscollection() == 0) {
                    map.put("iscollection", false);
                } else {
                    map.put("iscollection", true);
                }
            }
        } catch (Exception e) {
            status=-1;
            message="未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, map);
    }

    //endregion


}
