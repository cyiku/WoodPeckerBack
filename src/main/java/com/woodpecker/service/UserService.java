package com.woodpecker.service;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;

public interface UserService {

    User getUser(User user);
    Keyword getKeyword(User user);
}
