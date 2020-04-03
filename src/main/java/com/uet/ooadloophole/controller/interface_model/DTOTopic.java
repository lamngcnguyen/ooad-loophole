package com.uet.ooadloophole.controller.interface_model;

import com.uet.ooadloophole.model.business.TopicSpecFile;

import java.util.List;

public class DTOTopic {
    private String _id;
    private String name;
    private String descriptions;
    private String classId;
    private String groupId;
    private String groupName;
    private List<TopicSpecFile> files;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TopicSpecFile> getFiles() {
        return files;
    }

    public void setFiles(List<TopicSpecFile> files) {
        this.files = files;
    }
}
