package com.woodpecker.service.impl;

import com.woodpecker.domain.WeiboInfo;
import com.woodpecker.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service("MongoService")
public class MongoServiceImpl implements MongoService{
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(WeiboInfo weiboInfo) {
        mongoTemplate.save(weiboInfo);
    }

    public WeiboInfo findModel(String id) {
        System.out.println("finding model...");
        Query query = Query.query(Criteria.where("_id").is(id));
        WeiboInfo weiboInfo=mongoTemplate.findOne(query,WeiboInfo.class);
        return weiboInfo;
    }
}
