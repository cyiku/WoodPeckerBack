package com.woodpecker.domain;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "collectionContent")
public class UserCollection {
    @Id
    private String dataid;
    private Integer userid;
    private String type;
    private String data;
    public String getDataid(){return dataid;}
    public Integer getUserid(){return userid;}
    public String getType(){return type;}
    public String getData(){return data;}
    public void setDataid(String dataid){this.dataid=dataid;}
    public void setUserid(Integer userid){this.userid=userid;}
    public void setType(String type){this.type=type;}
    public void setData(String data){this.data=data;}
    public UserCollection() {

    }
    public UserCollection(String dataid,Integer userid,String type,String data){
        this.dataid=dataid;
        this.userid=userid;
        this.type=type;
        this.data=data;
    }
}
