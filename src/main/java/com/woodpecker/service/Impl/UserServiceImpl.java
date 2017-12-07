package com.woodpecker.service.Impl;

import com.woodpecker.dao.UserDao;
import com.woodpecker.domain.*;
import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //returns null if no such table exists
    public String existsTable(String tableName) {
        return userDao.existsTable(tableName);
    }

    public void newUser(String userName) {
        if (null == userDao.existsTable("collectionWeibo_" + userName))
            userDao.createCollectionWeibo("collectionWeibo_" + userName);
        if (null == userDao.existsTable("collectionTieba_" + userName))
            userDao.createCollectionTieba("collectionTieba_" + userName);
        if (null == userDao.existsTable("collectionTable_" + userName))
            userDao.createCollectionTable("collectionTable_" + userName);
        if (null == userDao.existsTable("keyword_" + userName))
            userDao.createKeyword("keyword_" + userName);
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
}