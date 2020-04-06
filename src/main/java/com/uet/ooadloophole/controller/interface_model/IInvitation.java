package com.uet.ooadloophole.controller.interface_model;

import java.util.List;

public class IInvitation {
    private String groupId;
    private List<String> members;
    private String message;

    public IInvitation(String groupId, List<String> members, String message) {
        this.groupId = groupId;
        this.members = members;
        this.message = message;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
