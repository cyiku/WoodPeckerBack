package com.woodpecker.domain;

public class TableCollection extends UserCollection {
    private Long tableid;
    private Integer iscollection;
    public Long getTableid(){return tableid;}
    public Integer getIscollection(){return iscollection;}
    public void setTableid(Long tableid){this.tableid=tableid;}
    public void setIscollection(Integer iscollection){this.iscollection=iscollection;}
    public TableCollection(String dataid, String data, Long tableid, Integer iscollection){
        this.setDataid(dataid);
        this.setData(data);
        this.tableid=tableid;
        this.iscollection=iscollection;
    }
}
