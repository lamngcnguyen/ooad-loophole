package com.uet.ooadloophole.controller.interface_model.dto;

import com.uet.ooadloophole.model.business.rup_elements.DisciplineFileType;

import java.util.List;

public class DTODiscipline {
    private String _id;
    private String name;
    private String description;
    private List<DisciplineFileType> disciplineFileTypeList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DisciplineFileType> getDisciplineFileTypeList() {
        return disciplineFileTypeList;
    }

    public void setDisciplineFileTypeList(List<DisciplineFileType> disciplineFileTypeList) {
        this.disciplineFileTypeList = disciplineFileTypeList;
    }
}
