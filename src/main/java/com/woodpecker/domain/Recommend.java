package com.woodpecker.domain;

public class Recommend {
    /**
     * words：所有推荐的关键字，空行隔开
     * date：关键字生成日期
     * keyword：关键词名称
     */
    public String words;
    public String date;
    public String keyword;
    
    public Recommend(String words, String date, String keyword) {
        this.words = words;
        this.date = date;
        this.keyword = keyword;
    }

    Recommend(){

    }

    public String getWords() {
        return this.words;
    }

    public String getDate(){
        return this.date;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserid(String keyword) {
        this.keyword = keyword;
    }
}
