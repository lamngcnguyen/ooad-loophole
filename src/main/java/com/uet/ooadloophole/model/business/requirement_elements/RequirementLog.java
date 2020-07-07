package com.uet.ooadloophole.model.business.requirement_elements;

import com.uet.ooadloophole.model.business.system_elements.Student;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

public class RequirementLog {
    @Id
    private String _id;
    private String whatChanged;
    @DBRef
    private Requirement requirement;
    private LocalDateTime datetimeModified;
    @DBRef
    private Student creator;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWhatChanged() {
        return whatChanged;
    }

    public void setWhatChanged(String whatChanged) {
        this.whatChanged = whatChanged;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public LocalDateTime getDatetimeModified() {
        return datetimeModified;
    }

    public void setDatetimeModified(LocalDateTime datetimeModified) {
        this.datetimeModified = datetimeModified;
    }

    public Student getCreator() {
        return creator;
    }

    public void setCreator(Student creator) {
        this.creator = creator;
    }
}
