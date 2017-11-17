package com.woodpecker.service;

import com.woodpecker.domain.UserCollection;
import com.woodpecker.domain.WeiboInfo;

import java.util.List;

public interface MongoService {
    public String insert(WeiboInfo weiboInfo);
    public String insert(UserCollection userCollection);
    public UserCollection findByContent(UserCollection userCollection);
    public void deleteByContent(UserCollection userCollection);
    public List<UserCollection> getByContent(UserCollection userCollection);
    public WeiboInfo findModel(String id);
}
