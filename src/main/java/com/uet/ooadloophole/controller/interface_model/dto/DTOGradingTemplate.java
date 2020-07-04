package com.uet.ooadloophole.controller.interface_model.dto;

import com.uet.ooadloophole.model.business.grading_elements.GradingTemplate;

import java.time.format.DateTimeFormatter;

public class DTOGradingTemplate {
    private String name;
    private String _id;
    private String timestamp;
    private int criteriaCount;

    public DTOGradingTemplate(GradingTemplate template) {
        this.name = template.getGradingTemplateName();
        this._id = template.get_id();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.timestamp = template.getTimestamp().format(dateTimeFormatter);
        if(template.getCriteria() != null) {
            this.criteriaCount = template.getCriteria().size();
        } else {
            this.criteriaCount = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCriteriaCount() {
        return criteriaCount;
    }
}
