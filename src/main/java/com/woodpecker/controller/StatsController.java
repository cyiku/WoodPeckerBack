package com.woodpecker.controller;

import com.woodpecker.domain.Topic;
import com.woodpecker.service.UserService;
import com.woodpecker.util.JSONResult;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class StatsController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/getDataSourceNum", method = RequestMethod.POST)
    public String getDataSrcNum(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            Map<String, Object> num = new HashMap<>();
            for (String append : appendList) {
                String tableName = keywordName + "_" + append;
                Integer count = 0;
                if (null != userService.existsTable(tableName)) {
                    count = userService.tableCount(tableName);
                }
                num.put(append, count);
            }
            result.put("num", num);
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/getPublishNum", method = RequestMethod.POST)
    public String getPubNum(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");
        try {
            String date;
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            List<String> dateList = new ArrayList<>();
            Boolean [] isExist = new Boolean[4];
            Map<String, Object> num = new HashMap<>();
            Calendar calender = new GregorianCalendar();
            calender.add(Calendar.DATE, -10);
            Integer tmp = 0;
            for(String append:appendList) {
                String tableName = keywordName + "_" + append;
                if (null == userService.existsTable(tableName)) {
                    num.put(append, new ArrayList<Integer>(Collections.nCopies(10,0)));
                    isExist[tmp++] = false;
                    continue;
                }
                isExist[tmp++] = true;
                List<Integer> numList = new ArrayList<>();
                num.put(append,numList);
            }
            for(int i=0;i<10;i++) {
                calender.add(Calendar.DATE, 1);
                date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));
                tmp = 0;
                for(String append:appendList) {
                    String tableName = keywordName + "_" + append;
                    //if (null == userService.existsTable(tableName)) continue;
                    if (!isExist[tmp++]) continue;
                    List<Integer> numList = (List<Integer>)num.get(append);
                    numList.add(userService.timeCount(tableName,date));
                }
            }
            result.put("date",dateList);
            result.put("num",num);
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/getPolarity", method = RequestMethod.POST)
    public String getPor(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");
        try {
            String date;
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            List<String> dateList = new ArrayList<>();
            List<Integer> posList = new ArrayList<>();
            List<Integer> negList = new ArrayList<>();
            Map<String, Object> num = new HashMap<>();

            Calendar calender = new GregorianCalendar();
            calender.add(Calendar.DATE, -10);
            num.put("positive",posList);
            num.put("negative",negList);

            for(int i=0;i<10;i++) {
                calender.add(Calendar.DATE, 1);
                date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));
                int posCount=0,negCount=0;
                for(String append:appendList) {
                    String tableName = keywordName + "_" + append;
                    if (null == userService.existsTable(tableName)) continue;
                    posCount+=userService.posTimeCount(tableName,date);
                    negCount+=userService.negTimeCount(tableName,date);
                }
                posList.add(posCount);
                negList.add(negCount);
            }
            result.put("date",dateList);
            result.put("num",num);
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }

    @RequestMapping(value = "/getClustering", method = RequestMethod.POST)
    public String getClustering(@RequestBody String info) {
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Topic> topicCollection = userService.getClustering();
            result.put("topic", topicCollection);
            result.put("time", topicCollection.get(0).getTime());
        } catch (Exception e) {
            status = -1;
            message = "未知错误";
            e.printStackTrace();
        }
        return JSONResult.fillResultString(status, message, result);
    }
}
