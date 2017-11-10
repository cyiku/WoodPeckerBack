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
    public List<Keyword> getKeyword(User user)
    {return userDao.getKeyword(user);}



}
