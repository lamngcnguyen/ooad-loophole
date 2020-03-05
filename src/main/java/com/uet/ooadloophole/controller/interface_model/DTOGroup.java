package com.uet.ooadloophole.controller.interface_model;

import com.uet.ooadloophole.model.business.Student;

import java.util.List;

public class DTOGroup {
    private String _id;
    private String groupName;
    private Student leader;
    private List<Student> members;

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

    public Student getLeader() {
        return leader;
    }

    public void setLeader(Student leader) {
        this.leader = leader;
    }

    public List<Student> getMembers() {
        return members;
    }

    public void setMembers(List<Student> members) {
        this.members = members;
    }
}
