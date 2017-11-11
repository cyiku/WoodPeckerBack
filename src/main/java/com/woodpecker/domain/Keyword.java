package com.woodpecker.domain;

public class Keyword {
    private Integer keywordid;
    private Integer userid;
    private String name;
    private String sites;
    public Keyword() {
    };
    public Keyword(Integer keywordid,Integer userid,String name,String sites){
        this.keywordid=keywordid;
        this.userid=userid;
        this.name=name;
        this.sites=sites;
    }

    public Integer getKeywordid() {return keywordid;}
    public Integer getUserid() {return userid;}
    public String getName(){return this.name;}
    public String getSites(){return this.sites;}

    public void setKeywordid(Integer keywordid) {this.keywordid=keywordid;}
    public void setUserid(Integer userid) {this.userid=userid;}
    public void setName(String name){this.name=name;}
    public void setSites(String sites){this.sites=sites;}
}
