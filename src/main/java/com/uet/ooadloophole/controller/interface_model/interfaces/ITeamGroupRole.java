package com.uet.ooadloophole.controller.interface_model.interfaces;

public class ITeamGroupRole {
    private String groupId;
    private String roleId;
    private boolean hasAccess;

    public ITeamGroupRole(String groupId, String roleId, boolean hasAccess) {
        this.groupId = groupId;
        this.roleId = roleId;
        this.hasAccess = hasAccess;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
