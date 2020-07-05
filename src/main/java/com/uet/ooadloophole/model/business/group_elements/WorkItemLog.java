package com.uet.ooadloophole.model.business.group_elements;

import com.uet.ooadloophole.model.business.system_elements.Student;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

public class WorkItemLog {
    @Id
    private String _id;
    @DBRef
    private Student student;
    @DBRef
    private WorkItem task;
    private String type;
    private String description;
    private LocalDateTime timeStamp;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public WorkItem getTask() {
        return task;
    }

    public void setTask(WorkItem task) {
        this.task = task;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
