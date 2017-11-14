package com.woodpecker.service;

import com.woodpecker.domain.WeiboInfo;

public interface MongoService {
    public void insert(WeiboInfo weiboInfo);
    public WeiboInfo findModel(String id);
}
