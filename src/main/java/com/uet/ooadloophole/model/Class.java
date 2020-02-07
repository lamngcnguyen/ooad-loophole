package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Class {
    @Id
    private String _id;
    private String className;
    private String teacherId; //Points to User
    private String semesterId;
    //The day of week the class supposed to take place
    //This variable's name might be changed later
    private int scheduledDayOfWeek;

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
}
