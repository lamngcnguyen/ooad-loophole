package com.uet.ooadloophole.controller.interface_model.interfaces;

import java.util.List;

public class IWorkItem {
    private String boardId;
    private String name;
    private String description;
    private String priority;
    private String creator;
    private List<String> assignedMember;
    private String status;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getAssignedMember() {
        return assignedMember;
    }

    public void setAssignedMember(List<String> assignedMember) {
        this.assignedMember = assignedMember;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
