package com.uet.ooadloophole.controller.interface_model;

public class DTOClass {
    private String _id;
    private String className;
    private String teacherName;
    private String semesterName;
    private int scheduledDayOfWeek;
    private int studentCount;
    private boolean active;

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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public int getScheduledDayOfWeek() {
        return scheduledDayOfWeek;
    }

    public void setScheduledDayOfWeek(int scheduledDayOfWeek) {
        this.scheduledDayOfWeek = scheduledDayOfWeek;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
