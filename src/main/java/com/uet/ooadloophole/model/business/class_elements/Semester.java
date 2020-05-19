package com.uet.ooadloophole.model.business.class_elements;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Semester {
    @Id
    private String _id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
