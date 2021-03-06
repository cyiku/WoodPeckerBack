package com.woodpecker.service.Impl;

import com.woodpecker.dao.UserDao;
import com.woodpecker.domain.*;
import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(User user) {
        return userDao.getUser(user);
    }

    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    public void newUser(String userName) {
        List<String> nameList = Arrays.asList("collectionForum_","collectionWeibo_","collectionPortal_",
                "collectionAgency_","collectionChart_");
        for(String name:nameList) {
            if(null == userDao.existsTable(name + userName))
                userDao.createCollectionNormal(name + userName);
        }
        if (null == userDao.existsTable("collectionTable_" + userName))
            userDao.createCollectionTable("collectionTable_" + userName);
    }

    //region keyword
    public List<Keyword> getKeyword(User user) {
        return userDao.getKeyword(user);
    }

    public Integer addKeyword(User user, Keyword keyword) {
        return userDao.addKeyword(user, keyword);
    }

    public List<Keyword> searchKeyword(User user, Keyword keyword) {
        return userDao.searchKeyword(user, keyword);
    }

    public Integer delKeyword(User user, Keyword keyword) {
        return userDao.delKeyword(user, keyword);
    }

    public Integer updateKeyword(User user, Keyword keyword) {
        return userDao.updateKeyword(user, keyword);
    }
    //endregion

    //region agency collection
    public List<NormalCollection> getAgencyCollection(User user) {
        return userDao.getNormalCollection("collectionAgency_" + user.getId());
    }

    public Integer addAgencyCollection(User user, NormalCollection agencyCollection) {
        return userDao.addNormalCollection("collectionAgency_" + user.getId(), agencyCollection);
    }

    public List<NormalCollection> searchAgencyCollection(User user, NormalCollection agencyCollection) {
        return userDao.searchNormalCollection("collectionAgency_" + user.getId(), agencyCollection);
    }

    public Integer resetAgencyCollection(User user, NormalCollection agencyCollection) {
        return userDao.resetNormalCollection("collectionAgency_" + user.getId(), agencyCollection);
    }

    public Integer delAgencyCollection(User user, NormalCollection agencyCollection) {
        return userDao.delNormalCollection("collectionAgency_" + user.getId(), agencyCollection);
    }
    //endregion

    //region chart collection
    public List<NormalCollection> getChartCollection(User user) {
        return userDao.getNormalCollection("collectionChart_" + user.getId());
    }

    public Integer addChartCollection(User user, NormalCollection chartCollection) {
        return userDao.addNormalCollection("collectionChart_" + user.getId(), chartCollection);
    }

    public List<NormalCollection> searchChartCollection(User user, NormalCollection chartCollection) {
        return userDao.searchNormalCollection("collectionChart_" + user.getId(), chartCollection);
    }

    public Integer resetChartCollection(User user, NormalCollection chartCollection) {
        return userDao.resetNormalCollection("collectionChart_" + user.getId(), chartCollection);
    }

    public Integer delChartCollection(User user, NormalCollection chartCollection) {
        return userDao.delNormalCollection("collectionChart_" + user.getId(), chartCollection);
    }
    //endregion

    //region forum collection
    public List<NormalCollection> getForumCollection(User user) {
        return userDao.getNormalCollection("collectionForum_" + user.getId());
    }

    public Integer addForumCollection(User user, NormalCollection forumCollection) {
        return userDao.addNormalCollection("collectionForum_" + user.getId(), forumCollection);
    }

    public List<NormalCollection> searchForumCollection(User user, NormalCollection forumCollection) {
        return userDao.searchNormalCollection("collectionForum_" + user.getId(), forumCollection);
    }

    public Integer resetForumCollection(User user, NormalCollection forumCollection) {
        return userDao.resetNormalCollection("collectionForum_" + user.getId(), forumCollection);
    }

    public Integer delForumCollection(User user, NormalCollection forumCollection) {
        return userDao.delNormalCollection("collectionForum_" + user.getId(), forumCollection);
    }
    //endregion

    //region portal collection
    public List<NormalCollection> getPortalCollection(User user) {
        return userDao.getNormalCollection("collectionPortal_" + user.getId());
    }

    public Integer addPortalCollection(User user, NormalCollection portalCollection) {
        return userDao.addNormalCollection("collectionPortal_" + user.getId(), portalCollection);
    }

    public List<NormalCollection> searchPortalCollection(User user, NormalCollection portalCollection) {
        return userDao.searchNormalCollection("collectionPortal_" + user.getId(), portalCollection);
    }

    public Integer resetPortalCollection(User user, NormalCollection portalCollection) {
        return userDao.resetNormalCollection("collectionPortal_" + user.getId(), portalCollection);
    }

    public Integer delPortalCollection(User user, NormalCollection portalCollection) {
        return userDao.delNormalCollection("collectionPortal_" + user.getId(), portalCollection);
    }
    //endregion

    //region weibo collection
    public List<NormalCollection> getWeiboCollection(User user) {
        return userDao.getNormalCollection("collectionWeibo_" + user.getId());
    }

    public Integer addWeiboCollection(User user, NormalCollection weiboCollection) {
        return userDao.addNormalCollection("collectionWeibo_" + user.getId(), weiboCollection);
    }

    public List<NormalCollection> searchWeiboCollection(User user, NormalCollection weiboCollection) {
        return userDao.searchNormalCollection("collectionWeibo_" + user.getId(), weiboCollection);
    }

    public Integer resetWeiboCollection(User user, NormalCollection weiboCollection) {
        return userDao.resetNormalCollection("collectionWeibo_" + user.getId(), weiboCollection);
    }

    public Integer delWeiboCollection(User user, NormalCollection weiboCollection) {
        return userDao.delNormalCollection("collectionWeibo_" + user.getId(), weiboCollection);
    }
    //endregion

    //region business collection
    public List<NormalCollection> getBusinessCollection(User user) {
        return userDao.getNormalCollection("collectionBusiness_" + user.getId());
    }

    public Integer addBusinessCollection(User user, NormalCollection normalCollection) {
        return userDao.addNormalCollection("collectionBusiness_" + user.getId(), normalCollection);
    }

    public List<NormalCollection> searchBusinessCollection(User user, NormalCollection normalCollection) {
        return userDao.searchNormalCollection("collectionBusiness_" + user.getId(), normalCollection);
    }

    public Integer resetBusinessCollection(User user, NormalCollection normalCollection) {
        return userDao.resetNormalCollection("collectionBusiness_" + user.getId(), normalCollection);
    }

    public Integer delBusinessCollection(User user, NormalCollection normalCollection) {
        return userDao.delNormalCollection("collectionBusiness_" + user.getId(), normalCollection);
    }
    //endregion

    //region business collection
    public List<NormalCollection> getIndustryCollection(User user) {
        return userDao.getNormalCollection("collectionIndustry_" + user.getId());
    }

    public Integer addIndustryCollection(User user, NormalCollection normalCollection) {
        return userDao.addNormalCollection("collectionIndustry_" + user.getId(), normalCollection);
    }

    public List<NormalCollection> searchIndustryCollection(User user, NormalCollection normalCollection) {
        return userDao.searchNormalCollection("collectionIndustry_" + user.getId(), normalCollection);
    }

    public Integer resetIndustryCollection(User user, NormalCollection normalCollection) {
        return userDao.resetNormalCollection("collectionIndustry_" + user.getId(), normalCollection);
    }

    public Integer delIndustryCollection(User user, NormalCollection normalCollection) {
        return userDao.delNormalCollection("collectionIndustry_" + user.getId(), normalCollection);
    }
    //endregion

    //region table collection
    public List<TableCollection> getTableCollection(User user) {
        return userDao.getTableCollection("collectionTable_" + user.getId());
    }

    public Integer addTableCollection(User user, List<TableCollection> tableCollection) {
        return userDao.addTableCollection("collectionTable_" + user.getId(), tableCollection);
    }

    public List<TableCollection> searchTableCollection(User user, List<TableCollection> tableCollection) {
        return userDao.searchTableCollection("collectionTable_" + user.getId(), tableCollection);
    }

    public Integer resetTableCollection(User user, List<TableCollection> tableCollection) {
        return userDao.resetTableCollection("collectionTable_" + user.getId(), tableCollection);
    }

    public Integer delTableCollection(User user, List<TableCollection> tableCollection) {
        return userDao.delTableCollection("collectionTable_" + user.getId(), tableCollection);
    }
    //endregion

    //region sites
    public List<Site> getSite() {
        return userDao.getSite();
    }
    //endregion

    //region utils

    //returns null if no such table exists
    public String existsTable(String tableName) {
        return userDao.existsTable(tableName);
    }
    //endregion

    //region stats
//    public Integer tableCount(String tableName) {
//        return userDao.tableCount(tableName);
//    }
    public List<Distribution> distributionCount(String keyword) {return userDao.distributionCount(keyword);}
    public List<Statistic> timeCount(String keyword, String dateStr) {
        return userDao.timeCount(keyword,dateStr);
    }
    public Integer posTimeCount(String tableName, String dateStr) {
        return userDao.posTimeCount(tableName,dateStr);
    }
    public Integer negTimeCount(String tableName, String dateStr) {
        return userDao.negTimeCount(tableName,dateStr);
    }
    public List<Sentiment> polarityCount(String keyword,String dateStr) { return userDao.polarityCount(keyword, dateStr); }
    //endregion

    public List<Topic> getClustering(String keyword) { return userDao.getClustering(keyword); }

    //region info
    public List<String> getInfo (String keywordName, String src) {
        String tableName = keywordName + "_" + src;
        System.out.println(tableName);
        return userDao.getInfo(tableName);
    }
    //endregion

    // region modify polarity
    public Integer addMsgPolarity(User user, MsgPolarity msgPolarity) {
        return userDao.addMsgPolarity(user, msgPolarity);
    }
    // endregion

    public List<Sentiment> polarityAllCount(String keyword) {
        return userDao.polarityAllCount(keyword);
    }

    public List<MsgPolarity> getModifyPolarity(User user) {
        return userDao.getModifyPolarity(user);
    }

    public Recommend getRecommend(String keyword){
        return userDao.getRecommend(keyword);
    }

    public Integer delRecommend(User user, Recommend recommend) {
        return userDao.delRecommend(user, recommend);
    }

    public List<Recommend> getDelRecommend(User user) {
        return userDao.getDelRecommend(user);
    }

    public void createCollectionNormal(String tableName) {
        userDao.createCollectionNormal(tableName);
    }
}