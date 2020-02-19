package com.uet.ooadloophole.controller.interface_model;

public class IStudent {
    private String fullName;
    private String studentId;
    private String classId;

    public IStudent(String fullName, String studentId, String classId) {
        this.fullName = fullName;
        this.studentId = studentId;
        this.classId = classId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
