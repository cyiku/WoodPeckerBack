package com.woodpecker.dao;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDao {

	public User getUser(User user);
	public List<Keyword> getKeyword(User user);
	public Integer addKeyword(Keyword keyword);
	public List<Keyword> searchKeyword(Keyword keyword);
	public Integer delKeyword(Keyword keyword);
	public Integer updateKeyword(Keyword keyword);
}
