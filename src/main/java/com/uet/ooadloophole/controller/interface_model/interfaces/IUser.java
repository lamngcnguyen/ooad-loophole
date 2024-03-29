package com.uet.ooadloophole.controller.interface_model.interfaces;

public class IUser {
    private String fullName;
    private String username;
    private String email;
    private Boolean isAdmin;
    private Boolean isTeacher;
    private String phoneNumber;

    public IUser(String fullName, String username, String email, boolean isAdmin, boolean isTeacher, String phoneNumber) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isTeacher = isTeacher;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
