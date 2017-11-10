package com.woodpecker.service;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;

import java.util.List;

public interface UserService {

    User getUser(User user);
    List<Keyword> getKeyword(User user);
    void addKeyword(Keyword keyword);
    void delKeyword(Keyword keyword);
    void updateKeyword(Keyword keyword);
}
