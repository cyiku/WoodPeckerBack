package com.example.demo.util;

import com.example.demo.domain.User;
import com.example.demo.security.JwtUser;
import com.example.demo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;

public class GetUser {

    public static JwtUser getPrincipal(){
        JwtUser jwtUser = null;
        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof JwtUser) {
            jwtUser = ((JwtUser)principal);
        }
        return jwtUser;
    }
}
