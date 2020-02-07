package com.uet.ooadloophole.model.implement_model;

public class IClass {
    private String className;
    private String teacherId;
    private String semesterId;
    private int scheduledDayOfWeek;

    public IClass(String className, String teacherId, String semesterId, int scheduledDayOfWeek) {
        this.className = className;
        this.teacherId = teacherId;
        this.semesterId = semesterId;
        this.scheduledDayOfWeek = scheduledDayOfWeek;
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

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public int getScheduledDayOfWeek() {
        return scheduledDayOfWeek;
    }

    public void setScheduledDayOfWeek(int scheduledDayOfWeek) {
        this.scheduledDayOfWeek = scheduledDayOfWeek;
    }
}
