package com.uet.ooadloophole.model.business.requirement_elements;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.rup_elements.Iteration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class Requirement {
    @Id
    private String _id;
    private String name;
    private String description;
    private String type;
    private int level;
    @DBRef
    private Iteration iteration;
    @DBRef
    private Requirement parentReq;
    @DBRef
    private WorkItem task;
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

    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public Requirement getParentReq() {
        return parentReq;
    }

    public void setParentReq(Requirement parentReq) {
        this.parentReq = parentReq;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<RequirementSpecFile> getRequirementSpecFile() {
        return requirementSpecFile;
    }

    public void setRequirementSpecFile(List<RequirementSpecFile> requirementSpecFile) {
        this.requirementSpecFile = requirementSpecFile;
    }

    public WorkItem getTask() {
        return task;
    }

    public void setTask(WorkItem task) {
        this.task = task;
    }
}
