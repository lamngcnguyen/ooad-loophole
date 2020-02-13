package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

public class Token {

    @Id
    private String id;
    private String tokenString;
    private Date createdTime;
    private String userEmail;

    public Token(String userEmail) {
        tokenString = UUID.randomUUID().toString();
        createdTime = new Date();
        this.userEmail = userEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUser(String userEmail) {
        this.userEmail = userEmail;
    }
}

