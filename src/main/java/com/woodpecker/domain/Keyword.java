package com.woodpecker.domain;

public class Keyword {
    private Integer id;
    private String name;
    private String sites;
    public Keyword() {
    };
    public Keyword(Integer id,String name,String sites){
        this.id=id;
        this.name=name;
        this.sites=sites;
    }

    public Integer getId() {return id;}
    public String getName(){return this.name;}
    public String getSites(){return this.sites;}

    public void setId(Integer id) {this.id=id;}
    public void setName(String name){this.name=name;}
    public void setSites(String sites){this.sites=sites;}
}
