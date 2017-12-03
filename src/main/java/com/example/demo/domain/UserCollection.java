package com.example.demo.domain;

public class UserCollection {
    private String dataid;
    private String data;
    public String getDataid(){return dataid;}
    public String getData(){return data;}
    public void setDataid(String dataid){this.dataid=dataid;}
    public void setData(String data){this.data=data;}
    public UserCollection() {
    }
    public UserCollection(String dataid,String data){
        this.dataid=dataid;
        this.data=data;
    }
}

