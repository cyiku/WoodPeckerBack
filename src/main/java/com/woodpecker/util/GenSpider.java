package com.woodpecker.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class GenSpider {

    private Configuration cfg;

    public GenSpider(String tmpDir) {

        // 指定版本号
        cfg=new Configuration(Configuration.VERSION_2_3_22);

        //设置模板目录
        try {
            cfg.setDirectoryForTemplateLoading(new File(tmpDir));
        } catch (Exception e) {
            System.out.println(e);
        }
        //设置默认编码格式
        cfg.setDefaultEncoding("UTF-8");

    }

    private void help(String tempPath, Map<String, Object> map, String filePath) throws Exception {

        // 获取模板
        Template temp = cfg.getTemplate(tempPath);

        // 生成输出文件
        File file = new File(filePath);
        FileOutputStream fileout = new FileOutputStream(file);
        Writer out = new OutputStreamWriter(fileout);

        // 输出
        temp.process(map, out);

        //关闭
        out.flush();
        out.close();
        fileout.close();
    }

    public void genDir(String projName) {

        File dir = new File(projName);

        if (dir.exists()) {
            System.out.println("创建目录" + projName + "失败，目标目录已经存在");
            return ;
        }
        if (!projName.endsWith(File.separator)) {
            projName = projName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + projName + "成功！");
        } else {
            System.out.println("创建目录" + projName + "失败！");
        }

    }

    public String processName(String projName) {
        String [] array = projName.split("_");
        String ans = "";
        // 第一个字母大写
        for (int i = 0; i < array.length; ++i) {
            String tmp = array[i];
            char c = array[i].charAt(0);
            if (c>='a'&&c<='z') {
                tmp = tmp.replace(tmp.charAt(0), (char)(tmp.charAt(0) - 32));
            }
            ans += tmp;
        }
        return ans;
    }

    public void genTmp(String projName) throws Exception {

        Map<String, Object> project = new HashMap<>();
        project.put("projName", projName);

        // 生成scrapy.cfg
        help("scrapy.cfg.ftl", project, projName + "/" + "scrapy.cfg");

        // 生成init.py
        new File(projName + "/" + projName + "/__init__.py").createNewFile();
        new File(projName + "/" + projName + "/spiders/__init__.py").createNewFile();

        // 生成middleware
        Map<String, Object> middleware = new HashMap<>();
        String afterProcess = processName(projName);
        middleware.put("Middleware", afterProcess + "SpiderMiddleware");
        middleware.put("DownloaderMiddleware", afterProcess + "DownloaderMiddleware");
        help("middlewares.ftl", middleware, projName + "/" + projName + "/" + "middlewares.py");

        // 生成settings
//        Map<String, Object> settings = new HashMap<>();
//        middleware.put("afterProcess", afterProcess);
//        middleware.put("projName", projName);
//        help("settings.ftl", middleware, projName + "/" + projName + "/" + "settings.py");
    }

    public void genSettings(String projName, String downloadDelay, Map<String, Object> mongoProp) throws Exception{
        Map<String, Object> settings = new HashMap<>();
        settings.put("projName", projName);
        settings.put("afterProcess", processName(projName));
        settings.put("MONGO_HOST", mongoProp.get("MONGO_HOST"));
        settings.put("MONGO_PORT", mongoProp.get("MONGO_PORT"));
        settings.put("MONGODB_DBNAME", mongoProp.get("MONGODB_DBNAME"));
        settings.put("MONGO_COLLECTION", mongoProp.get("MONGO_COLLECTION"));
        settings.put("DOWNLOAD_DELAY", downloadDelay);
        help("settings.ftl", settings, projName + "/" + projName + "/" + "settings.py");
    }

    public void genItem(String projName, String[] property) throws Exception {

        Map<String, Object> item = new HashMap<>();
        item.put("ClassItem", processName(projName) + "Item");
        item.put("properties", property);
//        switch (type) {
//            case "weibo":
//                tempPath = "item/weiboItem.ftl";
//                break;
//            case "agency":
//                tempPath = "item/agencyItem.ftl";
//                break;
//            case "tieba":
//                tempPath = "item/tiebaItem.ftl";
//                break;
//            case "menhu":
//                tempPath = "item/portalItem.ftl";
//                break;
//        }
        help("item.ftl", item, projName + "/" + projName + "/" + "items.py");
    }

    public void genSpider(String projName, String name, String[] allowed_domains, String[] start_urls, String itemRule, String[] properties, String[] propertyRule) throws Exception{
        Map<String, Object> spider = new HashMap<>();

        spider.put("projName", projName);
        spider.put("ClassItem", processName(projName) + "Item");

        spider.put("ClassSpider", processName(projName) + "Spider");
        spider.put("name", name);
        spider.put("allowed_domains", allowed_domains);
        spider.put("start_urls", start_urls);
        spider.put("itemRule", itemRule);

        Map<String, String> proToRule = new HashMap<>();
        for (int i = 0; i < properties.length; ++i) {
            proToRule.put(properties[i], propertyRule[i]);
        }
        spider.put("proToRule", proToRule);


        help("spider.ftl", spider, projName + "/" + projName + "/spiders/" + name + ".py");
    }

    public void genPipelines(String projName, String [] properties) throws Exception{
        Map<String, Object> pipelines = new HashMap<>();
        pipelines.put("ClassPipeline", processName(projName) + "Pipeline");
        pipelines.put("properties", properties);
        help("pipelines.ftl", pipelines, projName + "/" + projName + "/" + "pipelines.py");
    }

    public static void main(String[] args) throws Exception {

//        GenSpider test = new GenSpider("src/main/java/com/woodpecker/genScrapy/templates");
//
//        //测试
////        Map<String, Object> product = new HashMap<>();
////        product.put("name", "Huwei P8");
////        product.put("price", "3985.7");
////        product.put("users", new String[]{"Tom","Jack","Rose"});
////        test.help("product.ftl", product, "file.py");
//
//        String projName = "ab_cd";
//
//        // First Step: 生成工程目录
//        String projDir = projName + "/" + projName + "/" + "spiders";
//        test.genDir(projDir);
//
//        // Second Step: 根据项目名生成一些简单的模板
//        test.genTmp(projName);
//
//        // Third Step: 根据mongo生成settings
//        Map<String, Object> mongoProp = new HashMap<>();
//        mongoProp.put("MONGO_HOST", "localhost");
//        mongoProp.put("MONGO_PORT", "27017");
//        mongoProp.put("MONGODB_DBNAME", "Crawler");
//        mongoProp.put("MONGO_COLLECTION", projName);
//        test.genSettings(projName, mongoProp);
//
//        // Fourth Step: 根据类型生成相应的item
//        String type = "agency";
//        test.genItem(projName, type);
//
//        // Fifth Step: 生成具体的spider
//        String name = "spider";
//        String [] allowed_domains = {"htkaoyan.com"};
//        String [] start_urls = {
//                "http://www.htkaoyan.com"
//            };
//
//
//        String itemRule = "//ul[@class=\"top-list  fn-clear\"]/li";
//        String [] properties = {"name"};
//        String [] propertyRule = {"//ul[@class=\"top-list  fn-clear\"]/li"};
//        test.genSpider(projName, name, allowed_domains, start_urls, itemRule, properties, propertyRule);
//        test.genPipelines(projName, properties);

    }
}