package com.woodpecker.domain;

public class Recommend {
    /**
     * words：所有推荐的关键字，空行隔开
     * date：关键字生成日期
     * userid：用户Id
     */
    public String words;
    public String date;
    public int userid;
    
    public Recommend(String words, String date, int userid) {
        this.words = words;
        this.date = date;
        this.userid = userid;
    }

    Recommend(){

    }

    public String getWords() {
        return this.words;
    }

    public String getDate(){
        return this.date;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
