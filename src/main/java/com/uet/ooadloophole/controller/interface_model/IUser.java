package com.uet.ooadloophole.controller.interface_model;

import com.uet.ooadloophole.config.Constant;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.model.business.User;

import java.util.HashSet;
import java.util.Set;

public class IUser {
    private String fullName;
    private String username;
    private String email;
    private boolean isAdmin;
    private boolean isTeacher;

    public IUser(String fullName, String username, String email, boolean isAdmin, boolean isTeacher) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isTeacher = isTeacher;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
}
