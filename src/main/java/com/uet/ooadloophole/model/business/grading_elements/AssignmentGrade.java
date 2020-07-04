package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class AssignmentGrade {
    @Id
    private String _id;
    private String assignmentId;
    private String groupId;
    private double value;
    private LocalDateTime timestamp;

    public AssignmentGrade() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _Id) {
        this._id = _Id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
