package com.uet.ooadloophole.controller.interface_model.interfaces;

public class IAssignment {
    private String name;
    private String classId;
    private String description;
    private String deadline;
    private String gradingTemplateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getGradingTemplateId() {
        return gradingTemplateId;
    }

    public void setGradingTemplateId(String gradingTemplateId) {
        this.gradingTemplateId = gradingTemplateId;
    }
}
