package com.woodpecker.mongodb;

import java.util.Map;
import java.util.List;

public interface MongoBase<T> {
    //添加
    public void insert(T object, String collectionName);
    //查找
    public T findOne(Map<String,Object> params, String collectionName);
    //查找所有
    public List<T> findAll(Map<String,Object> params, String collectionName);
    //修改
    public void update(Map<String,Object> params, String collectionName);
    //创建Collection
    public void createCollection(String collectionName);
    //删除
    public void remove(Map<String,Object> params, String collectionName);
}
