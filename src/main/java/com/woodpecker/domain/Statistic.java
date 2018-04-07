package com.woodpecker.domain;

public class Statistic {
    String keyword;
    String source;
    String date;
    int count;

    Statistic(){

    }

    Statistic(String keyword, String source, String date, int count) {
        this.keyword = keyword;
        this.source = source;
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSource() {
        return source;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
