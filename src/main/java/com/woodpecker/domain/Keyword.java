package com.woodpecker.domain;

public class Keyword {
    private Integer id;
    private Integer userid;
    private String name;
    private String sites;
    public Keyword() {
    };
    public Keyword(Integer id,Integer userid,String name,String sites){
        this.id=id;
        this.userid=userid;
        this.name=name;
        this.sites=sites;
    }

    public Integer getId() {return id;}
    public Integer getUserid() {return userid;}
    public String getName(){return this.name;}
    public String getSites(){return this.sites;}

    public void setId(Integer id) {this.id=id;}
    public void setUserid(Integer userid) {this.userid=userid;}
    public void setName(String name){this.name=name;}
    public void setSites(String sites){this.sites=sites;}
}
