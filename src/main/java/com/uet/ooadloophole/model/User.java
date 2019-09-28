package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private Long _id;
    private String username;
    private String password;
    private String email;
    private String admin;
    private String firstName;
    private String lastName;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
