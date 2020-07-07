package com.uet.ooadloophole.model.business.requirement_elements;

import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

public class Requirement {
    @Id
    private String _id;
    private String name;
    private String description;
    private String type;
    private LocalDateTime datetimeCreated;
    @DBRef
    private Requirement parentReq;
    @DBRef
    private LoopholeUser creator;
    private List<RequirementSpecFile> requirementSpecFile;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Requirement getParentReq() {
        return parentReq;
    }

    public void setParentReq(Requirement parentReq) {
        this.parentReq = parentReq;
    }

    public List<RequirementSpecFile> getRequirementSpecFile() {
        return requirementSpecFile;
    }

    public void setRequirementSpecFile(List<RequirementSpecFile> requirementSpecFile) {
        this.requirementSpecFile = requirementSpecFile;
    }

    public LocalDateTime getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(LocalDateTime datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public LoopholeUser getCreator() {
        return creator;
    }

    public void setCreator(LoopholeUser creator) {
        this.creator = creator;
    }
}
