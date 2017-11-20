package com.woodpecker.service;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;

import java.util.List;

public interface UserService {
    String existsTable(String tableName);
    void newUser(String userName);

    User getUser(User user);

    List<Keyword> getKeyword(User user);
    Integer addKeyword(User user,Keyword keyword);
    List<Keyword> searchKeyword(User user,Keyword keyword);
    Integer delKeyword(User user,Keyword keyword);
    Integer updateKeyword(User user,Keyword keyword);
}
