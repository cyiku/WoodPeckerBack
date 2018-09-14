package com.woodpecker.domain;

public class Topic {
    /**
     * id: 第几个topic(0-19)
     * content: 每个topic下的内容
     * time: 聚类时间
     */
    private Integer id;
    private String content;
    private String time;

    public Topic(Integer id, String content, String time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {this.time = time;}

    public String getTime() {return this.time;}
}
