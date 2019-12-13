package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class Class {
    @Id
    private String _id;
    private String className;
    private String teacherId;
    private String startDate;
    private String endDate;
    //The day of week the class supposed to take place
    //This variable's name might be changed later
    private String scheduledDayOfWeek;
    private ArrayList<String> deadline;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getScheduledDayOfWeek() {
        return scheduledDayOfWeek;
    }

    public void setScheduledDayOfWeek(String scheduledDayOfWeek) {
        this.scheduledDayOfWeek = scheduledDayOfWeek;
    }

    public ArrayList<String> getDeadline() {
        return deadline;
    }

    public void setDeadline(ArrayList<String> deadline) {
        this.deadline = deadline;
    }
}
