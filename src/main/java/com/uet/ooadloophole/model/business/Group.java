package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

public class Group {
    @Id
    private String _id;
    private String groupName;
    private String classId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
