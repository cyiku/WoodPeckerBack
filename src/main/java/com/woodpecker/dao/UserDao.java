package com.woodpecker.dao;

import com.woodpecker.domain.User;
import com.woodpecker.domain.Keyword;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	public User getUser(User user);
	public Keyword getKeyword(User user);
}
