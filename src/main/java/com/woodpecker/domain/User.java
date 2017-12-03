package com.woodpecker.domain;

import java.util.Date;
import java.util.List;

public class User {

    private String username;
    private String password;
    private Integer id;
    private Date lastPasswordResetDate;
    private List<String> roles;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {return id;}

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Integer id) {this.id = id;}

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setLastPasswordResetDate(Date date) {
        this.lastPasswordResetDate = date;
    }

    public Date getLastPasswordResetDate() {return lastPasswordResetDate;}
}
