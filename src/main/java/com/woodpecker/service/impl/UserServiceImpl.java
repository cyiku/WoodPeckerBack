package com.woodpecker.service.impl;

import com.woodpecker.dao.UserDao;
import com.woodpecker.domain.NormalCollection;
import com.woodpecker.domain.TableCollection;
import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(User user){
        return userDao.getUser(user);
    }

    //returns null if no such table exists
    public String existsTable(String tableName){return userDao.existsTable(tableName);}
    public void newUser(String userName) {
        if(null==userDao.existsTable("collectionWeibo_" + userName))
            userDao.createCollectionWeibo("collectionWeibo_" + userName);
        if(null==userDao.existsTable("collectionTieba_" + userName))
            userDao.createCollectionTieba("collectionTieba_" + userName);
        if(null==userDao.existsTable("collectionTable_" + userName))
            userDao.createCollectionTable("collectionTable_" + userName);
        if(null==userDao.existsTable("keyword_" + userName))
            userDao.createKeyword("keyword_" + userName);
    }

    //region keyword
    public List<Keyword> getKeyword(User user) {
        return userDao.getKeyword("keyword_" + user.getId());
    }
    public Integer addKeyword(User user,Keyword keyword) {
        return userDao.addKeyword("keyword_" + user.getId(), keyword);
    }
    public List<Keyword> searchKeyword(User user,Keyword keyword) {
        return userDao.searchKeyword("keyword_" + user.getId(), keyword);
    }
    public Integer delKeyword(User user,Keyword keyword) {
        return userDao.delKeyword("keyword_" + user.getId(), keyword);
    }
    public Integer updateKeyword(User user,Keyword keyword) {
        return userDao.updateKeyword("keyword_" + user.getId(), keyword);
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

    //region tieba collection
    public List<NormalCollection> getTiebaCollection(User user) {
        return userDao.getNormalCollection("collectionTieba_" + user.getId());
    }
    public Integer addTiebaCollection(User user, NormalCollection TiebaCollection) {
        return userDao.addNormalCollection("collectionTieba_" + user.getId(), TiebaCollection);
    }
    public List<NormalCollection> searchTiebaCollection(User user, NormalCollection TiebaCollection) {
        return userDao.searchNormalCollection("collectionTieba_" + user.getId(), TiebaCollection);
    }
    public Integer resetTiebaCollection(User user, NormalCollection TiebaCollection) {
        return userDao.resetNormalCollection("collectionTieba_" + user.getId(), TiebaCollection);
    }
    public Integer delTiebaCollection(User user, NormalCollection TiebaCollection) {
        return userDao.delNormalCollection("collectionTieba_" + user.getId(), TiebaCollection);
    }
    //endregion

    //region table collection
    public List<TableCollection> getTableCollection(User user) {
        return userDao.getTableCollection("collectionTable_" + user.getId());
    }
    public Integer addTableCollection(User user,TableCollection tableCollection) {
        return userDao.addTableCollection("collectionTable_" + user.getId(), tableCollection);
    }
    public List<TableCollection> searchTableCollection(User user,TableCollection tableCollection) {
        return userDao.searchTableCollection("collectionTable_" + user.getId(), tableCollection);
    }
    public Integer resetTableCollection(User user,TableCollection tableCollection) {
        return null;
        //return userDao.resetTableCollection("collectionTable_" + user.getId(), tableCollection);
    }
    public Integer delTableCollection(User user,TableCollection tableCollection) {
        return userDao.delTableCollection("collectionTable_" + user.getId(), tableCollection);
    }
    //endregion
}

