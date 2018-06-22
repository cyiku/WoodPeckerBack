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
    /**
     * 关键词分析页面的Controller
     */

    @Resource
    private UserService userService;

    @RequestMapping(value = "/getDataSourceNum", method = RequestMethod.POST)
    public String getDataSrcNum(@RequestBody String info) {
        /**
         * 获取相关关键字中的四个大类的总数量
         */
        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");

            // num存放四大类的消息数量
            Map<String, Object> num = new HashMap<>();
            
            // 查看数据分布表是否存在
            if (null != userService.existsTable("distribution_t")) {

                // 获取四个大类的数量并放入num中
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
        /**
         * 获取四个大类中的近10天每天的消息数量
         */

        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> appendList = Arrays.asList("forum", "weibo", "portal", "agency");
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");

            // dateList: 近10天日期
            List<String> dateList = new ArrayList<>();

            // num：key：四个大类(String)，value：近10天的每天的消息数目
            Map<String, List<Integer>> num = new HashMap<>();

            // append即四个大类中的一个
            for(String append:appendList) {
                List<Integer> numList = new ArrayList<>();
                num.put(append,numList);
            }

            // 将时区设置成东八区，并把日期设置成10天前
            Calendar calender = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            calender.setTimeZone(timeZone);
            calender.add(Calendar.DATE, -10);
            
            for(int i=0;i<10;i++) {
                // 日期+1天
                calender.add(Calendar.DATE, 1);
                // 将日期由Calendar转为string并保存到dateList中
                String date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));

                // 获取指定日期，关键字的四个来源的数量
                List<Statistic> statistics = userService.timeCount(keywordName,date);
                for (Statistic statistic: statistics) {
                    Integer count = statistic.getCount();
                    if (statistic.getSource().equals("培训机构")){
                        num.get("agency").add(count);
                    } else if (statistic.getSource().equals("微博")){
                        num.get("weibo").add(count);
                    } else if (statistic.getSource().equals("论坛")){
                        num.get("forum").add(count);
                    } else if (statistic.getSource().equals("门户网站")){
                        num.get("portal").add(count);
                    }
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
        /**
         * 获取四个大类中的近10天每天的情感分析数量
         */
        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 将info的格式由String转为jsonObject
            JSONObject jsonObject = new JSONObject(info);
            String keywordName = (String) jsonObject.get("keyword");

            // dateList: 近10天日期，posList: 近10天情感极性为正的消息数目
            List<String> dateList = new ArrayList<>();
            List<Integer> posList = new ArrayList<>();
            List<Integer> negList = new ArrayList<>();
            List<Integer> neuList = new ArrayList<>();
            
            // 保存每个极性的数目
            Map<String, List<Integer>> num = new HashMap<>();
            num.put("positive",posList);
            num.put("negative",negList);
            num.put("neutral",neuList);

            // 将时区设置成东八区，并把日期设置成10天前
            Calendar calender = new GregorianCalendar();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
            calender.setTimeZone(timeZone);
            calender.add(Calendar.DATE, -10);

            for(int i=0;i<10;i++) {
                // 日期+1天
                calender.add(Calendar.DATE, 1);
                // 将日期由Calendar转为string并保存到dateList中
                String date=String.format("%04d_%02d_%02d",calender.get(Calendar.YEAR),
                        1+calender.get(Calendar.MONTH),calender.get(Calendar.DATE));//month starts with 1
                dateList.add(date.replaceAll("_","-"));
                
                // 保存指定日期三个极性的数量
                int posCount=0,negCount=0, neutralCount=0;

                // 获取指定日期三个极性的数量
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
        /**
         * 获取话题聚类
         */
        // status: 状态码，message: 存储错误信息，result: 存放返回结果
        Integer status = 1;
        String message = "";
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            // 获取聚类结果
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


