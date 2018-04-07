package com.woodpecker.domain;

public class Distribution {
    public String keyword;
    public String source;
    public int count;
    public Distribution(){

    }
    public Distribution(String keyword, String source, int count) {
        this.keyword = keyword;
        this.source = source;
        this.count = count;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
