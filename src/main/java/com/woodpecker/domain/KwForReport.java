package com.woodpecker.domain;

public class KwForReport {
    public String name;
    public String timeNow;
    public int weiboNum;
    public int forumNum;
    public int portalNum;
    public int agencyNum;
    public int allMsgNum;
    public int posNum;
    public int negNum;
    public int neuNum;

    public KwForReport() {

    }

    public KwForReport(String name, String timeNow, int weiboNum, int forumNum, int portalNum, int agencyNum, int allMsgNum,
                       int posNum, int negNum, int neuNum) {
        this.name = name;
        this.timeNow = timeNow;
        this.weiboNum = weiboNum;
        this.forumNum = forumNum;
        this.portalNum = portalNum;
        this.agencyNum = agencyNum;
        this.allMsgNum = allMsgNum;
        this.posNum = posNum;
        this.negNum = negNum;
        this.neuNum = neuNum;
    }

    public int getAgencyNum() {
        return agencyNum;
    }

    public int getAllMsgNum() {
        return allMsgNum;
    }

    public int getForumNum() {
        return forumNum;
    }

    public int getNegNum() {
        return negNum;
    }

    public int getNeuNum() {
        return neuNum;
    }

    public int getPortalNum() {
        return portalNum;
    }

    public int getPosNum() {
        return posNum;
    }

    public int getWeiboNum() {
        return weiboNum;
    }

    public String getName() {
        return name;
    }

    public String getTimeNow() {
        return timeNow;
    }

    public void setAgencyNum(int agencyNum) {
        this.agencyNum = agencyNum;
    }

    public void setAllMsgNum(int allMsgNum) {
        this.allMsgNum = allMsgNum;
    }

    public void setForumNum(int forumNum) {
        this.forumNum = forumNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNegNum(int negNum) {
        this.negNum = negNum;
    }

    public void setNeuNum(int neuNum) {
        this.neuNum = neuNum;
    }

    public void setPortalNum(int portalNum) {
        this.portalNum = portalNum;
    }

    public void setPosNum(int posNum) {
        this.posNum = posNum;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }

    public void setWeiboNum(int weiboNum) {
        this.weiboNum = weiboNum;
    }
}
