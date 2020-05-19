package com.uet.ooadloophole.model.business.requirement_elements;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class Requirement {
    @Id
    private String _id;
    private String name;
    private String parentId;
    private String description;
    @DBRef
    private List<Requirement> childRequirements;
    private RequirementSpecFile requirementSpecFile;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Requirement> getChildRequirements() {
        return childRequirements;
    }

    public void setChildRequirements(List<Requirement> childRequirements) {
        this.childRequirements = childRequirements;
    }

    public RequirementSpecFile getRequirementSpecFile() {
        return requirementSpecFile;
    }

    public void setRequirementSpecFile(RequirementSpecFile requirementSpecFile) {
        this.requirementSpecFile = requirementSpecFile;
    }
}
