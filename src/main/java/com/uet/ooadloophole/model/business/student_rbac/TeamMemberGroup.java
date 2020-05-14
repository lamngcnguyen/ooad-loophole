package com.uet.ooadloophole.model.business.student_rbac;

import org.springframework.data.annotation.Id;

public class TeamMemberGroup {
    @Id
    private String _id;
    private String teamMemberId; // studentId
    private String teamGroupId; // groupId

    public TeamMemberGroup() {

    }

    public TeamMemberGroup(String teamMemberId, String teamGroupId) {
        this.teamMemberId = teamMemberId;
        this.teamGroupId = teamGroupId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(String teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public String getTeamGroupId() {
        return teamGroupId;
    }

    public void setTeamGroupId(String teamGroupId) {
        this.teamGroupId = teamGroupId;
    }
}
