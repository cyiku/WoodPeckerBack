package com.woodpecker.domain;

public class Sentiment {
    public String keyword;
    public String date;
    public int sentiment;
    public int count;
    public Sentiment(){

    }
    public Sentiment(String keyword, String date, int sentiment, int count) {
        this.keyword = keyword;
        this.date = date;
        this.sentiment = sentiment;
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getCount() {
        return count;
    }

    public int getSentiment() {
        return sentiment;
    }

    public String getDate() {
        return date;
    }
}
