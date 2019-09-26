package com.uet.ooadloophole.models;

import javax.persistence.*;

@Entity
@Table(name = "user")

public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
