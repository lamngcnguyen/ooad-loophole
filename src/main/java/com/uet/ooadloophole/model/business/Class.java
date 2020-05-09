package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

public class Class {
    @Id
    private String _id;
    private String className;
    private String teacherId; //Points to LoopholeUser
    private String semesterId;
    private int scheduledDayOfWeek;
    private int limit;
    private boolean active;

    public Class() {

    }

    public Class(String className, String teacherId, String semesterId, int scheduledDayOfWeek) {
        this.className = className;
        this.teacherId = teacherId;
        this.semesterId = semesterId;
        this.scheduledDayOfWeek = scheduledDayOfWeek;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getScheduledDayOfWeek() {
        return scheduledDayOfWeek;
    }

    public void setScheduledDayOfWeek(int scheduledDayOfWeek) {
        this.scheduledDayOfWeek = scheduledDayOfWeek;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
