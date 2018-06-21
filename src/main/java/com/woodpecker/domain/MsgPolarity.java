package com.woodpecker.domain;

public class MsgPolarity {
    /**
     * source：消息的来源，小来源，不是四大类
     * id：消息的id
     * polarity：修改后的情感极性
     */

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
