package com.uet.ooadloophole.model;

public class StudentDTO {
    public String userId;
    public String fullName;
    public String studentId;
    public String groupId;
    public String groupName;
    public String email;

    public StudentDTO(String userId, String fullName, String email, String studentId, String groupId, String groupName) {
        this.userId = userId;
        this.fullName = fullName;
        this.studentId = studentId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.email = email;
    }
}
