package com.woodpecker.service.impl;

import com.woodpecker.dao.UserDao;
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

    public List<Keyword> getKeyword(User user)
    {return userDao.getKeyword("keyword_" + user.getId());}

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
}

