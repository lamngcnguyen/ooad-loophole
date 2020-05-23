package com.uet.ooadloophole.controller.interface_model.dto;

import com.uet.ooadloophole.model.business.rup_elements.Discipline;

public class DTOClassDisciplineConfig {
    private String _id;
    private String classId;
    private Discipline discipline;
    private boolean enabled;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
