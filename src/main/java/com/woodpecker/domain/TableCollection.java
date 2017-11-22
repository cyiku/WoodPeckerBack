package com.woodpecker.domain;

public class TableCollection extends UserCollection {
    private Long tableid;
    public Long getTableid(){return tableid;}
    public void setTableid(Long tableid){this.tableid=tableid;}
    public TableCollection(Integer dataid, String data, Long tableid){
        this.setDataid(dataid);
        this.setData(data);
        this.tableid=tableid;
    }
}
