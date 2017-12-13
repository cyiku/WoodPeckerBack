package com.woodpecker.domain;

public class Topic {

    private Integer id;
    private String word;
    private String time;

    public Topic(Integer id, String word, String time) {
        this.id = id;
        this.word = word;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTime(String time) {this.time = time;}

    public String getTime() {return this.time;}
}
