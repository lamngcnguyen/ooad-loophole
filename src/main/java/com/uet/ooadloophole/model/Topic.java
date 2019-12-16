package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Topic {
    @Id
    private String _id;
    private String name;
    private String descriptions;
    private String classId;
    private List<UserFile> specificationFiles;

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

    public List<UserFile> getSpecificationFiles() {
        return specificationFiles;
    }

    public void setSpecificationFiles(List<UserFile> specificationFiles) {
        this.specificationFiles = specificationFiles;
    }
}
