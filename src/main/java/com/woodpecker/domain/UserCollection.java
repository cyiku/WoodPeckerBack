package com.woodpecker.domain;

import org.json.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "collectionContent")
public class UserCollection {
    @Id
    private String id;
    private Integer userid;
    private String type;
    private JSONArray data;
    public String getId(){return id;}
    public Integer getUserid(){return userid;}
    public String getType(){return type;}
    public JSONArray getData(){return data;}
    public void setId(String id){this.id=id;}
    public void setId(Integer userid){this.userid=userid;}
    public void setType(String type){this.type=type;}
    public void setData(JSONArray data){this.data=data;}
    public UserCollection() {

    }
    public UserCollection(String id,Integer userid,String type,JSONArray data){
        this.id=id;
        this.userid=userid;
        this.type=type;
        this.data=data;
    }
}
