package com.woodpecker.dao;
import com.woodpecker.domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
	// spring security
	public User findByUserName(String username);
	// 注册
	public Integer insert(User user);

	//region 创建表相关

	public void createCollectionNormal(String tableName);
	public void createCollectionTable(String tableName);
	//endregion

	//region user, keyword相关
	public User getUser(User user);
	public List<Keyword> getKeyword(@Param("user")User user);
	public Integer addKeyword(@Param("user")User user, @Param("keyword") Keyword keyword);
	public List<Keyword> searchKeyword(@Param("user")User user, @Param("keyword") Keyword keyword);
	public Integer delKeyword(@Param("user")User user, @Param("keyword") Keyword keyword);
	public Integer updateKeyword(@Param("user")User user, @Param("keyword") Keyword keyword);
	//endregion

	//region collection相关
	public List<NormalCollection> getNormalCollection(@Param("tableName")String tableName);
	public Integer addNormalCollection(
			@Param("tableName")String tableName,
			@Param("normalCollection")NormalCollection normalCollection);
	public List<NormalCollection> searchNormalCollection(
			@Param("tableName")String tableName,
			@Param("normalCollection")NormalCollection normalCollection);
	public Integer resetNormalCollection(
			@Param("tableName")String tableName,
			@Param("normalCollection")NormalCollection normalCollection);
	public Integer delNormalCollection(
			@Param("tableName")String tableName,
			@Param("normalCollection")NormalCollection normalCollection);

	public List<TableCollection> getTableCollection(@Param("tableName")String tableName);
	public Integer addTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")List<TableCollection> tableCollection);
	public List<TableCollection> searchTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")List<TableCollection> tableCollection);
	public Integer resetTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")List<TableCollection> tableCollection);
	public Integer delTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")List<TableCollection> tableCollection);

	//endregion

	//region sites
	public List<Site> getSite();
	//endregion

	//region utils
	public String existsTable(String tableName);
	public Integer tableCount(@Param("tableName")String tableName);
	//endregion


}
