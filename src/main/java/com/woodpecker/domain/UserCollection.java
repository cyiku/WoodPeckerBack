package com.woodpecker.domain;

public class UserCollection {
    private Integer dataid;
    private String data;
    public Integer getDataid(){return dataid;}
    public String getData(){return data;}
    public void setDataid(Integer dataid){this.dataid=dataid;}
    public void setData(String data){this.data=data;}
    public UserCollection() {
    }
    public UserCollection(Integer dataid,String data){
        this.dataid=dataid;
        this.data=data;
    }
}

