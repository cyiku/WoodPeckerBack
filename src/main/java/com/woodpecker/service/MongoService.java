package com.woodpecker.service;

import com.woodpecker.domain.UserCollection;
import com.woodpecker.domain.WeiboInfo;

import java.util.List;

public interface MongoService {
    //UserCollection Services
    public String insert(UserCollection userCollection);
    public UserCollection findByData(UserCollection userCollection);
    public void deleteByDataid(UserCollection userCollection);
    public List<UserCollection> getByUser(UserCollection userCollection);
    public UserCollection getByDataid(UserCollection userCollection);

    //WeiboInfo Services
    public String insert(WeiboInfo weiboInfo);
    public List<WeiboInfo> getByKeyword(WeiboInfo weiboInfo);
}
