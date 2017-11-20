package com.woodpecker.domain;

public class Keyword {
    private Integer keywordid;
    private String name;
    private String sites;
    public Keyword() {
    };
    public Keyword(Integer keywordid,String name,String sites){
        this.keywordid=keywordid;
        this.name=name;
        this.sites=sites;
    }

    public Integer getKeywordid() {return keywordid;}
    public String getName(){return this.name;}
    public String getSites(){return this.sites;}

    public void setKeywordid(Integer keywordid) {this.keywordid=keywordid;}
    public void setName(String name){this.name=name;}
    public void setSites(String sites){this.sites=sites;}
}
