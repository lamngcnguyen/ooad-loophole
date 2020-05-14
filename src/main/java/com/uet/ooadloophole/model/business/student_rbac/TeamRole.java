package com.uet.ooadloophole.model.business.student_rbac;

import org.springframework.data.annotation.Id;

public class TeamRole {
    @Id
    private String _id;
    private String teamRole;
    private String description;

    public TeamRole() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
