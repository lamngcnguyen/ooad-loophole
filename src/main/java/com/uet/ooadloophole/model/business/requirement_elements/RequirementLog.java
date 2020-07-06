package com.uet.ooadloophole.model.business.requirement_elements;

import com.uet.ooadloophole.model.business.system_elements.Student;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

public class RequirementLog {
    @Id
    private String _id;
    private String description;
    @DBRef
    private Requirement requirement;
    @DBRef
    private Student creator;
    private String type;
    private LocalDateTime time;
    private RequirementSpecFile files;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getCreator() {
        return creator;
    }

    public void setCreator(Student creator) {
        this.creator = creator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public RequirementSpecFile getFiles() {
        return files;
    }

    public void setFiles(RequirementSpecFile files) {
        this.files = files;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }
}
