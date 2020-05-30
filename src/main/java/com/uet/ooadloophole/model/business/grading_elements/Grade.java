package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Grade {
    @Id
    private String _id;
    private String graderId;
    private String iterationId;
    private int value;
    private LocalDateTime timestamp;

    Grade () {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _Id) {
        this._id = _Id;
    }

    public String getGraderId() {
        return graderId;
    }

    public void setGraderId(String graderId) {
        this.graderId = graderId;
    }

    public String getIterationId() {
        return iterationId;
    }

    public void setIterationId(String iterationId) {
        this.iterationId = iterationId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
