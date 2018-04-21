package com.woodpecker.controller;

import com.woodpecker.domain.*;
import com.woodpecker.security.JwtUser;
import com.woodpecker.service.UserService;
import com.woodpecker.util.GetUser;
import com.woodpecker.util.WordGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class BriefReportController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/briefReport", method = RequestMethod.POST)
    public void addCollection(@RequestBody String info, HttpServletResponse resp) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        JwtUser jwtUser = GetUser.getPrincipal();
        User user = userService.findByUserName(jwtUser.getUsername());

        // keyword number
        List<Keyword> keywords = userService.getKeyword(user);
        int keywordNum = keywords.size();
        map.put("keywordNum", keywordNum);

        // keywordList
        List<KwForReport> keywordList = new ArrayList<> ();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");  //东八区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(timeZone);
        String time = sdf.format(new Date());
        for (Keyword keyword: keywords) {
            String keywordName = keyword.getName();

            List<Distribution> distributions = userService.distributionCount(keywordName);
            int weiboNum=0, forumNum=0, portalNum=0, agencyNum=0, allMsgNum=0;
            for(Distribution d: distributions) {
                String source = d.getSource();
                if (source.equals("培训机构")) {
                    agencyNum = d.getCount();
                } else if(source.equals("微博")) {
                    weiboNum = d.getCount();
                } else if(source.equals("论坛")) {
                    forumNum = d.getCount();
                }else if(source.equals("门户网站")) {
                    portalNum = d.getCount();
                }
                allMsgNum += d.getCount();
            }

            List<Sentiment> sentiments = userService.polarityAllCount(keywordName);
            int posNum = 0, negNum=0, neuNum=0, sentimentNum=0;
            for(Sentiment sentiment: sentiments) {
                int num = sentiment.getCount();
                if (sentiment.getSentiment() == 3) {
                    posNum += num;
                } else if (sentiment.getSentiment() == 2) {
                    negNum += num;
                } else if (sentiment.getSentiment() == 1) {
                    neuNum += num;
                }
                sentimentNum += num;
            }


            KwForReport kw = new KwForReport(keywordName, time, weiboNum,forumNum,portalNum,agencyNum,allMsgNum,posNum,negNum,neuNum);
            keywordList.add(kw);
        }
        map.put("keywordList", keywordList);


        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            // 调用工具类WordGenerator的createDoc方法生成Word文档
            file = WordGenerator.createDoc(map, "briefReport");
            fin = new FileInputStream(file);

            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件默认名为resume.doc
            resp.addHeader("Content-Disposition", "attachment;filename=resume.doc");

            out = resp.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(fin != null) fin.close();
            if(out != null) out.close();
            if(file != null) file.delete(); // 删除临时文件
        }
    }
}
