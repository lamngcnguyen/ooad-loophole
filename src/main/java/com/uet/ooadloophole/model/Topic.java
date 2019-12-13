package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class Topic {
    @Id
    private String _id;
    private String name;
    private String details;
    private String classId;
    private ArrayList<UserFile> specificationFiles;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public ArrayList<UserFile> getSpecificationFiles() {
        return specificationFiles;
    }

    public void setSpecificationFiles(ArrayList<UserFile> specificationFiles) {
        this.specificationFiles = specificationFiles;
    }
}
