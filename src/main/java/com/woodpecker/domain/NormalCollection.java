package com.woodpecker.domain;

public class NormalCollection extends UserCollection {
    private Integer iscollection;
    public Integer getIscollection(){return iscollection;}
    public void setIscollection(Integer iscollection){this.iscollection=iscollection;}
    public NormalCollection(Integer dataid,String data,Integer iscollection){
        this.setDataid(dataid);
        this.setData(data);
        this.iscollection=iscollection;
    }
}
