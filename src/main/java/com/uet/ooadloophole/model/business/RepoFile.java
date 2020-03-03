package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

public class RepoFile extends UserFile {
    @Id
    private String _id;
    private String groupId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
