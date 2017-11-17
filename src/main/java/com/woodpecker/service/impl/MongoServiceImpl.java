package com.woodpecker.service.impl;

import com.woodpecker.domain.User;
import com.woodpecker.domain.UserCollection;
import com.woodpecker.domain.WeiboInfo;
import com.woodpecker.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("MongoService")
public class MongoServiceImpl implements MongoService{
    @Autowired
    private MongoTemplate mongoWeibo;
    @Autowired
    private MongoTemplate mongoUserCollection;

    public String insert(WeiboInfo weiboInfo) {
        mongoWeibo.save(weiboInfo);
        return null;
    }

    public String insert(UserCollection userCollection) {
        System.out.println(userCollection.getData().getString("content"));
        mongoUserCollection.save(userCollection);
        return userCollection.getId();
    }

    public UserCollection findByContent(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("dataid").is(userCollection.getDataid()).and("data").is(userCollection.getData()));
        UserCollection result=mongoUserCollection.findOne(query,UserCollection.class);
        return result;
    }

    public void deleteByContent(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("dataid").is(userCollection.getDataid()));
        mongoUserCollection.remove(query,UserCollection.class);
    }

    public List<UserCollection> getByContent(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("dataid").is(userCollection.getDataid()));
        List<UserCollection> result=mongoUserCollection.find(query,UserCollection.class);
        return result;
    }

    public WeiboInfo findModel(String id) {
        System.out.println("finding model...");
        Query query = Query.query(Criteria.where("_id").is(id));
        WeiboInfo weiboInfo=mongoWeibo.findOne(query,WeiboInfo.class);
        return weiboInfo;
    }
}