package com.uet.ooadloophole.controller.interface_model.interfaces;

public class IStudentTeamGroup {
    private String studentId;
    private String teamGroupId;
    private boolean belongsTo;

    public IStudentTeamGroup(String studentId, String teamGroupId, boolean belongsTo) {
        this.studentId = studentId;
        this.teamGroupId = teamGroupId;
        this.belongsTo = belongsTo;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeamGroupId() {
        return teamGroupId;
    }

    public void setTeamGroupId(String teamGroupId) {
        this.teamGroupId = teamGroupId;
    }

    public boolean belongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(boolean belongsTo) {
        this.belongsTo = belongsTo;
    }
}
