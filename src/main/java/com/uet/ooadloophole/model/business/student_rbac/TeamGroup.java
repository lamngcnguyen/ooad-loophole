package com.uet.ooadloophole.model.business.student_rbac;

import org.springframework.data.annotation.Id;

public class TeamGroup {
    @Id
    private String _id;
    private String teamGroup;
    private String description;

    public TeamGroup() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeamGroup() {
        return teamGroup;
    }

    public void setTeamGroup(String teamGroup) {
        this.teamGroup = teamGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
