package com.woodpecker.domain;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "collectionContent")
public class UserCollection {
    @Id
    private String id;
    private Integer userid;
    private String dataid;
    private String type;
    private JSONObject data;
    public String getId(){return id;}
    public Integer getUserid(){return userid;}
    public String getDataid(){return dataid;}
    public String getType(){return type;}
    public JSONObject getData(){return data;}
    public void setId(String id){this.id=id;}
    public void setId(Integer userid){this.userid=userid;}
    public void setDataid(String dataid){this.dataid=dataid;}
    public void setType(String type){this.type=type;}
    public void setData(JSONObject data){this.data=data;}
    public UserCollection() {

    }
    public UserCollection(String id,Integer userid,String dataid,String type,JSONObject data){
        this.id=id;
        this.userid=userid;
        this.dataid=dataid;
        this.type=type;
        this.data=data;
    }
}
