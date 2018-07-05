package com.woodpecker.controller;

import com.woodpecker.domain.NormalCollection;
// 表格收藏，暂时没必要做
// import com.woodpecker.domain.TableCollection;
import com.woodpecker.domain.User;
import com.woodpecker.security.JwtUser;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import com.woodpecker.util.GetUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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

    public String HandleString(String str) {
        // 去掉一些违规字符
        if(StringUtils.isEmpty(str)) {
            return str;
        } else {
            return str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]","[emoji]")
                    .replaceAll("'","''")
                    .replaceAll("\\\\","\\\\\\\\");
        }
    }

    @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
    public String addCollection(@RequestBody String info) {
        
        // status: 状态码，message: 存储错误信息，map: 存放返回结果
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // type:四大类，data:具体数据
            String type = (String) jsonObject.get("type");
            JSONArray data =(JSONArray) jsonObject.get("data");
            String dataid;

            //获取时间戳，生出数据的id
            Date date = new Date(); 

            if(type.equals("table")) {
                // 表格收藏，暂时没必要做
                // List<TableCollection> tableCollections = new ArrayList<>();
                // for(int i=0;i<data.length();i++) {
                //     JSONObject o=(JSONObject)data.get(i);
                //     dataid = String.valueOf(o.get("id"));
                //     System.out.println(o.toString());
                //     data_str = HandleString(o.toString());
                //     tableCollections.add(new TableCollection(dataid,data_str,date.getTime(),1));
                // }
                // List<TableCollection> tableList = userService.searchTableCollection(user,tableCollections);
                // if(tableList.isEmpty()) {
                //     userService.addTableCollection(user,tableCollections);
                // }
                // else {
                //     userService.resetTableCollection(user,tableList);
                // }
            }
            else {
                // 检查数据是否有Id
                if(data.getJSONObject(0).has("_id")) {
                    dataid = String.valueOf(data.getJSONObject(0).get("_id"));
                } else {
                    dataid = String.valueOf(date.getTime()) + String.valueOf(data.getJSONObject(0).hashCode());
                }
                String data_str = HandleString(data.getJSONObject(0).toString());
                // 生成collection，为了以后查找是否存在该collection
                NormalCollection normalCollection = new NormalCollection(dataid, data_str, 1);
                List<NormalCollection> normalCollectionList;
                switch(type) {
                    case "agency":
                        // 检查是否存在该collection
                        normalCollectionList = userService.searchAgencyCollection(user, normalCollection);
                        if (normalCollectionList.isEmpty()) {
                            // 不存在则插入
                            userService.addAgencyCollection(user, normalCollection);
                        } else {
                            // 存在，此时是以前收藏过该消息，后来把取消收藏后，数据只是把iscollection置为0
                            // 并没有真正删除该消息，此时把isCollection置为1即可
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
                    case "business":
                        normalCollectionList = userService.searchBusinessCollection(user, normalCollection);
                        if (normalCollectionList.isEmpty()) {
                            userService.addBusinessCollection(user, normalCollection);
                        } else {
                            normalCollection = normalCollectionList.get(0);
                            userService.resetBusinessCollection(user, normalCollection);
                        }
                        break;
                    case "industry":
                        normalCollectionList = userService.searchIndustryCollection(user, normalCollection);
                        if (normalCollectionList.isEmpty()) {
                            userService.addIndustryCollection(user, normalCollection);
                        } else {
                            normalCollection = normalCollectionList.get(0);
                            userService.resetIndustryCollection(user, normalCollection);
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
        
        // status: 状态码，message: 存储错误信息，map: 存放返回结果
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // type:四大类，data:具体数据
            String type = (String) jsonObject.get("type");
            
            // 要删除的消息的id，考虑到表的情况，用Array存储
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;
            List<JSONObject> result;

            if(type.equals("table")) {
                // 表格收藏，暂时没必要做
                // List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                // for(int i=0;i<dataidList.length();i++) {
                //     dataid=String.valueOf(dataidList.get(i));
                //     tableCollections.add(new TableCollection(dataid,null,null,null));
                // }
                // userService.delTableCollection(user,tableCollections);
                // List<TableCollection> tableList = userService.getTableCollection(user);
                // Long tableid = null;
                // List<List<JSONObject>> resultList = new ArrayList<List<JSONObject>>();
                // result = null;
                // for(TableCollection table:tableList) {
                //     if(null == tableid) {
                //         tableid = table.getTableid();
                //         result = new ArrayList<JSONObject>();
                //         result.add(new JSONObject(table.getData()));
                //     }
                //     else if(tableid.equals(table.getTableid())) {
                //         result.add(new JSONObject(table.getData()));
                //     }
                //     else {
                //         tableid = table.getTableid();
                //         resultList.add(result);
                //         result = new ArrayList<JSONObject>();
                //         result.add(new JSONObject(table.getData()));
                //     }
                // }
                // if(null!=result)resultList.add(result);
                // map.put("collection", resultList);
            }
            else {
                // 不是表格的话，只需要取Array得第一个就好
                dataid = String.valueOf(dataidList.get(0));

                // 生成Collection，用于删除
                NormalCollection normalCollection = new NormalCollection(dataid, null, null);
                List<NormalCollection> normalCollectionList;
                switch (type) {
                    case "agency":
                        // 删除collection
                        userService.delAgencyCollection(user,normalCollection);
                        // 这里为什么又重新获取了一遍新的collection用于返回
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
                    case "business":
                        userService.delBusinessCollection(user,normalCollection);
                        normalCollectionList = userService.getBusinessCollection(user);
                        break;
                    case "industry":
                        userService.delIndustryCollection(user,normalCollection);
                        normalCollectionList = userService.getIndustryCollection(user);
                        break;
                    default:
                        normalCollectionList = new ArrayList<>();
                        System.out.println("unknown type " + type);
                        break;
                }

                // 将新的消息转化类型后返回
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
        
        // status: 状态码，message: 存储错误信息，map: 存放返回结果
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<>();
        try {

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // type:四大类
            String type = (String) jsonObject.get("type");

            List<JSONObject> result;

            if(type.equals("table")) {
                // 表格收藏，暂时没必要做
                // List<TableCollection> tableList = userService.getTableCollection(user);
                // System.out.println(tableList.size());
                // Long tableid = null;
                // List<List> resultList = new ArrayList<List>();
                // result = null;
                // for(TableCollection table:tableList) {
                //     if(null == tableid) {
                //         tableid = table.getTableid();
                //         result = new ArrayList<JSONObject>();
                //         result.add(new JSONObject(table.getData()));
                //     }
                //     else if(tableid.equals(table.getTableid())) {
                //         result.add(new JSONObject(table.getData()));
                //     }
                //     else {
                //         tableid = table.getTableid();
                //         resultList.add(result);
                //         result = new ArrayList<JSONObject>();
                //         result.add(new JSONObject(table.getData()));
                //     }
                // }
                // if(null!=result)resultList.add(result);
                // map.put("collection", resultList);
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
                    case "business":
                        normalCollectionList = userService.getBusinessCollection(user);
                        break;
                    case "industry":
                        normalCollectionList = userService.getIndustryCollection(user);
                        break;
                    default:
                        normalCollectionList = new ArrayList<>();
                        System.out.println("default");
                }
                result = new ArrayList<JSONObject>();
                // 转换格式后返回
                for(NormalCollection normalCollection:normalCollectionList) {
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
        
        // status: 状态码，message: 存储错误信息，map: 存放返回结果
        Integer status=1;
        String message="";
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            // 获取用户信息，为以后查询用户信息做准备
            JwtUser jwtUser = GetUser.getPrincipal();
            User user = userService.findByUserName(jwtUser.getUsername());

            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);

            // type:四大类
            String type = (String) jsonObject.get("type");

            // 要删除的消息的id，考虑到表的情况，用Array存储
            JSONArray dataidList = ((JSONArray) jsonObject.get("dataid"));
            String dataid;

            // 生成Collection，用于查询
            NormalCollection normalCollection;
            List<NormalCollection> normalCollectionList;
            if(type.equals("table")) {
                // List<TableCollection> tableCollections = new ArrayList<TableCollection>();
                // for(int i=0;i<dataidList.length();i++) {
                //     dataid = String.valueOf(dataidList.get(i));
                //     tableCollections.add(new TableCollection(dataid,null,null,null));
                // }
                // List<TableCollection> tableList = userService.searchTableCollection(user,tableCollections);
                // if(tableList.isEmpty()) {
                //     map.put("iscollection",false);
                // }
                // else {
                //     Boolean result = true;
                //     for(TableCollection tableCollection: tableList) {
                //         if(tableCollection.getIscollection() == 0) {
                //             result=false;
                //             break;
                //         }
                //     }
                //     map.put("iscollection",result);
                // }
            }
            else {
                // 不是表格的话，只需要取Array得第一个就好
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
                    case "business":
                        normalCollectionList = userService.searchBusinessCollection(user, normalCollection);
                        break;
                    case "industry":
                        normalCollectionList = userService.searchIndustryCollection(user, normalCollection);
                        break;
                    default:
                        normalCollectionList = new ArrayList<>();
                        System.out.println("unknown type " + type);
                        break;
                }
                if (normalCollectionList.isEmpty()) {
                    // 没找到该Id则没收藏
                    map.put("iscollection", false);
                } else if (normalCollectionList.get(0).getIscollection() == 0) {
                    // 找到该id了但isCollection已经置位0了也是没收藏
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
}
