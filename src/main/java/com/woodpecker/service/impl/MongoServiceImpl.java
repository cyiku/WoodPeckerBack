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
        mongoUserCollection.save(userCollection);
        return userCollection.getId();
    }

    public UserCollection findByData(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("data").is(userCollection.getData())
                .and("userid").is(userCollection.getUserid()).and("type").is(userCollection.getType()));
        UserCollection result=mongoUserCollection.findOne(query,UserCollection.class);
        return result;
    }

    public void deleteById(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("_id").is(userCollection.getId()));
        mongoUserCollection.remove(query,UserCollection.class);
    }

    public List<UserCollection> getByUser(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("userid").is(userCollection.getUserid())
                .and("type").is(userCollection.getType()));
        return mongoUserCollection.find(query,UserCollection.class);
    }

    public UserCollection getById(UserCollection userCollection) {
        Query query = Query.query(Criteria.where("_id").is(userCollection.getId()));
        return mongoUserCollection.findOne(query,UserCollection.class);
    }

    public List<WeiboInfo> getByKeyword(WeiboInfo weiboInfo) {
        System.out.println("finding model...");
        Query query = Query.query(Criteria.where("keyword").is(weiboInfo.getKeyword()));
        List<WeiboInfo> result=mongoWeibo.find(query,WeiboInfo.class);
        return result;
    }
}