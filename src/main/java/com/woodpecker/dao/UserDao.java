package com.woodpecker.dao;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDao {

	public User getUser(User user);
	public List<Keyword> getKeyword(User user);
	public void addKeyword(Keyword keyword);
	public void delKeyword(Keyword keyword);
	public void updateKeyword(Keyword keyword);
}
