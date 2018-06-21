package com.woodpecker.controller;

import com.woodpecker.domain.Distribution;
import com.woodpecker.domain.Sentiment;
import com.woodpecker.domain.Statistic;
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
//        List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");

        try {
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            Map<String, Object> num = new HashMap<>();
//            for (String append : appendList) {
//                String tableName = keywordName + "_" + append;
//                Integer count = 0;
//                if (null != userService.existsTable(tableName)) {
//                    count = userService.tableCount(tableName);
//                }
//
//                num.put(append, count);
//            }
            if (null != userService.existsTable("distribution_t")) {
                List<Distribution> distributions = userService.distributionCount(keywordName);
                for(Distribution d: distributions) {
                    String source = d.getSource();
                    if (source.equals("培训机构")) {
                        num.put("agency", d.getCount());
                    } else if(source.equals("微博")) {
                        num.put("weibo", d.getCount());
                    } else if(source.equals("论坛")) {
                        num.put("forum", d.getCount());
                    }else if(source.equals("门户网站")) {
                        num.put("portal", d.getCount());
                    }
                }
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
            // Boolean [] isExist = new Boolean[4];
            Map<String, Object> num = new HashMap<>();
            Calendar calender = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            calender.setTimeZone(timeZone);
            calender.add(Calendar.DATE, -10);
            // Integer tmp = 0;
            for(String append:appendList) {
//                String tableName = keywordName + "_" + append;
//                if (null == userService.existsTable(tableName)) {
//                    num.put(append, new ArrayList<Integer>(Collections.nCopies(10,0)));
//                    isExist[tmp++] = false;
//                    continue;
//                }
//                isExist[tmp++] = true;
                List<Integer> numList = new ArrayList<>();
                num.put(append,numList);
            }
            for(int i=0;i<10;i++) {
                calender.add(Calendar.DATE, 1);
                date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));
//                tmp = 0;
                List<Statistic> statistics = userService.timeCount(keywordName,date);
                for (Statistic statistic: statistics) {
                    if (statistic.getSource().equals("培训机构")){
                        List<Integer> numList = (List<Integer>)num.get("agency");
                        numList.add(statistic.getCount());
                    } else if (statistic.getSource().equals("微博")){
                        List<Integer> numList = (List<Integer>)num.get("weibo");
                        numList.add(statistic.getCount());
                    } else if (statistic.getSource().equals("论坛")){
                        List<Integer> numList = (List<Integer>)num.get("forum");
                        numList.add(statistic.getCount());
                    } else if (statistic.getSource().equals("门户网站")){
                        List<Integer> numList = (List<Integer>)num.get("portal");
                        numList.add(statistic.getCount());
                    }
                }
//                for(String append:appendList) {
//                    String tableName = keywordName + "_" + append;
//                    //if (null == userService.existsTable(tableName)) continue;
////                    if (!isExist[tmp++]) continue;
//                    List<Integer> numList = (List<Integer>)num.get(append);
//                    numList.add(userService.timeCount(tableName,date));
//                }
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
        // List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");
        try {
            String date;
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");
            List<String> dateList = new ArrayList<>();
            List<Integer> posList = new ArrayList<>();
            List<Integer> negList = new ArrayList<>();
            List<Integer> neuList = new ArrayList<>();
            Map<String, Object> num = new HashMap<>();

            Calendar calender = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            calender.setTimeZone(timeZone);
            calender.add(Calendar.DATE, -10);
            num.put("positive",posList);
            num.put("negative",negList);
            num.put("neutral",neuList);

            for(int i=0;i<10;i++) {
                calender.add(Calendar.DATE, 1);
                date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));
                int posCount=0,negCount=0, neutralCount=0;
//                for(String append:appendList) {
//                    String tableName = keywordName + "_" + append;
//                    if (null == userService.existsTable(tableName)) continue;
//                    List<Integer> count = userService.polarityCount(tableName, date);
//                    for (int j = 0; j < count.size(); ++j) {
//                        if (count.get(j) == 0)
//                            negCount += 1;
//                        else
//                            posCount += 1;
//                    }
//                }
                List<Sentiment> sentimentCount = userService.polarityCount(keywordName, date);
                for(Sentiment oneSentiment: sentimentCount) {
                    if (oneSentiment.getSentiment() == 3) {
                        posCount = oneSentiment.getCount();
                    } else if(oneSentiment.getSentiment() == 2) {
                        negCount = oneSentiment.getCount();
                    } else if(oneSentiment.getSentiment() == 1) {
                        neutralCount = oneSentiment.getCount();
                    }
                }

                posList.add(posCount);
                negList.add(negCount);
                neuList.add(neutralCount);
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


