package com.woodpecker.service;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;

import java.util.List;

public interface UserService {

    User getUser(User user);
    List<Keyword> getKeyword(User user);
    boolean addKeyword(Keyword keyword);
}
