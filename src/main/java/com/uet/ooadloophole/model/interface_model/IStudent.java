package com.uet.ooadloophole.model.interface_model;

public class IStudent {
    private String fullName;
    private String classId;
    private String groupName;
    private String groupId;
    private String studentId;
    private String email;

    public IStudent(String fullName, String groupName, String studentId, String email) {
        this.fullName = fullName;
        this.groupName = groupName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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