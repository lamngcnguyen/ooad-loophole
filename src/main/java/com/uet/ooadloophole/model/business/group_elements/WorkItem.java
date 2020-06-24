package com.uet.ooadloophole.model.business.group_elements;

import com.uet.ooadloophole.model.business.rup_elements.Iteration;
import com.uet.ooadloophole.model.business.system_elements.Student;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

public class WorkItem {
    @Id
    private String _id;
    private String boardId;
    private String name;
    private String description;
    private String priority;
    private LocalDateTime assignedDate;
    private LocalDateTime createdDateTime;
    @DBRef
    private Student creator;
    @DBRef
    private List<Student> assignedMember;
    @DBRef
    private List<WorkItemFile> workItemFiles;
    @DBRef
    private Iteration iteration;
    private String status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Student getCreator() {
        return creator;
    }

    public void setCreator(Student creator) {
        this.creator = creator;
    }

    public List<Student> getAssignedMember() {
        return assignedMember;
    }

    public void setAssignedMember(List<Student> assignedMember) {
        this.assignedMember = assignedMember;
    }

    public List<WorkItemFile> getWorkItemFiles() {
        return workItemFiles;
    }

    public void setWorkItemFiles(List<WorkItemFile> workItemFiles) {
        this.workItemFiles = workItemFiles;
    }

    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
