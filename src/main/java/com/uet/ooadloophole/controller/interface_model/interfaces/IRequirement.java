package com.uet.ooadloophole.controller.interface_model.interfaces;

import java.time.LocalDateTime;

public class IRequirement {
    private String name;
    private String description;
    private String type;
    private String groupId;
    private String parentReqId;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getParentReqId() {
        return parentReqId;
    }

    public void setParentReqId(String parentReqId) {
        this.parentReqId = parentReqId;
    }
}
