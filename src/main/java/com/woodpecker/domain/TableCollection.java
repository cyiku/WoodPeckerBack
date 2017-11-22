package com.woodpecker.domain;

public class TableCollection extends UserCollection {
    private Integer tableid;
    public Integer getTableid(){return tableid;}
    public void setTableid(Integer tableid){this.tableid=tableid;}
    public TableCollection(Integer dataid, String data, Integer tableid){
        this.setDataid(dataid);
        this.setData(data);
        this.tableid=tableid;
    }
}
