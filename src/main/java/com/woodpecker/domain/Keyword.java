package com.woodpecker.domain;

public class Keyword {
    private String name;
    private String sites;
    public Keyword() {
    };
    public Keyword(String name,String sites){
        this.name=name;
        this.sites=sites;
    }

    public String getName(){return this.name;}
    public String getSites(){return this.sites;}

    public void setName(String name){this.name=name;}
    public void setSites(String sites){this.sites=sites;}
}
