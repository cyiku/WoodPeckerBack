package com.woodpecker.util;

import com.woodpecker.security.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetUser {

    public static JwtUser getPrincipal(){
        JwtUser jwtUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof JwtUser) {
            jwtUser = ((JwtUser)principal);
        }
        return jwtUser;
    }
}
