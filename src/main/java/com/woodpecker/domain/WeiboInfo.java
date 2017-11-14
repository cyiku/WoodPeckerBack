package com.woodpecker.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "2017_11_05")
public class WeiboInfo{
    @Id
    private String id;
    private String keyword;
    private String url;
    private String content;
    private String time;
    public String getId(){return id;}
    public String getKeyword(){return keyword;}
    public String getUrl(){return url;}
    public String getContent(){return content;}
    public String getTime(){return time;}
    public void setId(String id){this.id=id;}
    public void setKeyword(String keyword){this.keyword=keyword;}
    public void setUrl(String url){this.url=url;}
    public void setContent(String content){this.content=content;}
    public void setTime(String time){this.time=time;}
    public WeiboInfo(String id, String keyword, String url, String content, String time) {
        this.id=id;
        this.keyword=keyword;
        this.url=url;
        this.content=content;
        this.time=time;
    }
}
