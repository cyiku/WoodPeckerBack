package com.woodpecker.service;

import com.woodpecker.domain.*;

import java.util.List;

public interface UserService {
    String existsTable(String tableName);

    //region stats
    public Integer tableCount(String tableName);
    public Integer timeCount(String tableName, String dateStr);
    public Integer posTimeCount(String tableName, String dateStr);
    public Integer negTimeCount(String tableName, String dateStr);
    //endregion

    void newUser(String userName);//为新用户创建表

    User getUser(User user);
    User findByUserName(String username);

    //region keyword
    List<Keyword> getKeyword(User user);
    Integer addKeyword(User user, Keyword keyword);
    List<Keyword> searchKeyword(User user, Keyword keyword);
    Integer delKeyword(User user, Keyword keyword);
    Integer updateKeyword(User user, Keyword keyword);
    //endregion

    //region normal collection
    //weibo
    List<NormalCollection> getAgencyCollection(User user);
    Integer addAgencyCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchAgencyCollection(User user, NormalCollection normalCollection);
    Integer resetAgencyCollection(User user, NormalCollection normalCollection);
    Integer delAgencyCollection(User user, NormalCollection normalCollection);

    List<NormalCollection> getChartCollection(User user);
    Integer addChartCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchChartCollection(User user, NormalCollection normalCollection);
    Integer resetChartCollection(User user, NormalCollection normalCollection);
    Integer delChartCollection(User user, NormalCollection normalCollection);

    List<NormalCollection> getForumCollection(User user);
    Integer addForumCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchForumCollection(User user, NormalCollection normalCollection);
    Integer resetForumCollection(User user, NormalCollection normalCollection);
    Integer delForumCollection(User user, NormalCollection normalCollection);

    List<NormalCollection> getPortalCollection(User user);
    Integer addPortalCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchPortalCollection(User user, NormalCollection normalCollection);
    Integer resetPortalCollection(User user, NormalCollection normalCollection);
    Integer delPortalCollection(User user, NormalCollection normalCollection);

    List<NormalCollection> getWeiboCollection(User user);
    Integer addWeiboCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchWeiboCollection(User user, NormalCollection normalCollection);
    Integer resetWeiboCollection(User user, NormalCollection normalCollection);
    Integer delWeiboCollection(User user, NormalCollection normalCollection);
    //endregion

    //region table collection
    List<TableCollection> getTableCollection(User user);
    Integer addTableCollection(User user,List<TableCollection> tableCollection);
    List<TableCollection> searchTableCollection(User user,List<TableCollection> tableCollection);
    Integer resetTableCollection(User user,List<TableCollection> tableCollection);
    Integer delTableCollection(User user,List<TableCollection> tableCollection);
    //endregion

    //region sites
    List<Site> getSite();
    //endregion

    //region topic
    List<Topic> getClustering();
    //endregion

    //region info
    List<String> getInfo (String keywordName, String src);
    //endregion
}
