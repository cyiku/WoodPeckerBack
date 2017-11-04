package com.woodpecker.service.impl;

import com.woodpecker.dao.UserDao;
import com.woodpecker.domain.User;
import com.woodpecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public User login(User user){
        return userDao.login(user);
    }


}
