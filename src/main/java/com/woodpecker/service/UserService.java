package com.woodpecker.service;

import com.woodpecker.domain.NormalCollection;
import com.woodpecker.domain.TableCollection;
import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;

import java.util.List;

public interface UserService {
    String existsTable(String tableName);
    void newUser(String userName);

    User getUser(User user);

    //region keyword
    List<Keyword> getKeyword(User user);
    Integer addKeyword(User user,Keyword keyword);
    List<Keyword> searchKeyword(User user,Keyword keyword);
    Integer delKeyword(User user,Keyword keyword);
    Integer updateKeyword(User user,Keyword keyword);
    //endregion

    //region weibo collection
    List<NormalCollection> getWeiboCollection(User user);
    Integer addWeiboCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchWeiboCollection(User user, NormalCollection normalCollection);
    Integer resetWeiboCollection(User user, NormalCollection normalCollection);
    Integer delWeiboCollection(User user, NormalCollection normalCollection);
    //endregion

    //region tieba collection
    List<NormalCollection> getTiebaCollection(User user);
    Integer addTiebaCollection(User user, NormalCollection normalCollection);
    List<NormalCollection> searchTiebaCollection(User user, NormalCollection normalCollection);
    Integer resetTiebaCollection(User user, NormalCollection normalCollection);
    Integer delTiebaCollection(User user, NormalCollection normalCollection);
    //endregion

    //region table collection
    List<TableCollection> getTableCollection(User user);
    Integer addTableCollection(User user,List<TableCollection> tableCollection);
    List<TableCollection> searchTableCollection(User user,List<TableCollection> tableCollection);
    Integer resetTableCollection(User user,List<TableCollection> tableCollection);
    Integer delTableCollection(User user,List<TableCollection> tableCollection);
    //endregion
}
