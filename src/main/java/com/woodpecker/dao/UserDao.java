package com.woodpecker.dao;

import com.woodpecker.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	public User getUser(User user);

}
