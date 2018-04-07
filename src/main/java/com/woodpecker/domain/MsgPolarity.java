package com.woodpecker.domain;

public class MsgPolarity {

    private String source;
    private String id;
    private String polarity;

    public MsgPolarity(String source, String id, String polarity) {
        this.source = source;
        this.id = id;
        this.polarity = polarity;
    }
    public MsgPolarity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
