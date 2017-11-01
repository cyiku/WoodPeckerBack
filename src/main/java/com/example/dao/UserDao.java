package com.example.dao;

import com.example.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	public User login(User user);

}
