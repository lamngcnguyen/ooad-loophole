package com.uet.ooadloophole.model.business.student_rbac;

import org.springframework.data.annotation.Id;

public class TeamGroupRole {
    @Id
    private String _id;
    private String teamGroupId;
    private String teamRoleId;

    public TeamGroupRole() {

    }

    public TeamGroupRole(String teamGroupId, String teamRoleId) {
        this.teamGroupId = teamGroupId;
        this.teamRoleId = teamRoleId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeamGroupId() {
        return teamGroupId;
    }

    public void setTeamGroupId(String teamGroupId) {
        this.teamGroupId = teamGroupId;
    }

    public String getTeamRoleId() {
        return teamRoleId;
    }

    public void setTeamRoleId(String teamRoleId) {
        this.teamRoleId = teamRoleId;
    }
}
