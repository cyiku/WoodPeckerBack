package com.woodpecker.service;

import com.woodpecker.mongodb.WeiboInfo;

public interface MongoService {
    public void insert(WeiboInfo weiboInfo);
    public WeiboInfo findModel(String id);
}
