package com.woodpecker.dao;

import com.woodpecker.domain.NormalCollection;
import com.woodpecker.domain.TableCollection;
import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDao {

	//region 创建表相关
	public String existsTable(String tableName);

	public void createCollectionWeibo(String tableName);
	public void createCollectionTieba(String tableName);
	public void createCollectionTable(String tableName);
	public void createKeyword(String tableName);
	//endregion

	//region user, keyword相关
	public User getUser(User user);
	public List<Keyword> getKeyword(@Param("tableName") String tableName);
	public Integer addKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public List<Keyword> searchKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public Integer delKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
	public Integer updateKeyword(@Param("tableName") String tableName, @Param("keyword") Keyword keyword);
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
			@Param("tableCollection")TableCollection tableCollection);
	public List<TableCollection> searchTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")TableCollection tableCollection);
	public Integer resetTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")TableCollection tableCollection);
	public Integer delTableCollection(
			@Param("tableName")String tableName,
			@Param("tableCollection")TableCollection tableCollection);

	//endregion
}
