package com.woodpecker.domain;

public class Site {
    /**
     * 网站类：
     * name：网站名称，如新浪微博，新浪网
     * type：四大类中的一个
     * tableName：mongo中表的名称，es的索引
     */

    private String name;
    private String type;
    private String tableName;

    public Site() {
    }

    public Site(String name,String type, String tableName){
        this.name=name;
        this.type=type;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
