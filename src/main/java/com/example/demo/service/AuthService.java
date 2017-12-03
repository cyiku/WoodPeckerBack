package com.example.demo.service;


import com.example.demo.domain.User;

public interface AuthService {
    User register(User userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}
