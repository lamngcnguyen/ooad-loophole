package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Template {
    @Id
    private String _id;
    private String name;
    private String teacherId;
    private LocalDateTime timestamp;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
