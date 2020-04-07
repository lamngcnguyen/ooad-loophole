package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

public class Student {
    @Id
    private String _id;
    private String userId;
    private String classId;
    private String groupId;
    private String studentId;
    private String fullName;

    public Student() {
    }

    public Student(String fullName, String studentId) {
        this.fullName = fullName;
        this.studentId = studentId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
