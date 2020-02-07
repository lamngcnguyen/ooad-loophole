package com.uet.ooadloophole.model.interface_model;

public class IStudent {
    private String fullName;
    private String classId;
    private String groupId;
    private String studentId;
    private String email;

    public IStudent(String fullName, String classId, String groupId, String studentId, String email) {
        this.fullName = fullName;
        this.classId = classId;
        this.groupId = groupId;
        this.studentId = studentId;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}