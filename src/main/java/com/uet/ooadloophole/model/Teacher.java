package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

public class Teacher {
    @Id
    private String _id;
    private String userId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
