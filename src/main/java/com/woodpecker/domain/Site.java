package com.woodpecker.domain;

public class Site {

    private String name;
    private String type;

    public Site() {
    }

    public Site(String name,String type){
        this.name=name;
        this.type=type;
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
}
