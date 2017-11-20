package com.woodpecker.dao;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDao {
	//创建表相关
	public String existsTable(String tableName);
	public void createCollectionWeibo(String tableName);
	public void createCollectionTieba(String tableName);
	public void createCollectionTable(String tableName);
	public void createKeyword(String tableName);

	//keyword相关
	public User getUser(User user);
	public List<Keyword> getKeyword(@Param("tableName") String tableName);
	public Integer addKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public List<Keyword> searchKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public Integer delKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public Integer updateKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
}
