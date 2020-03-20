package com.uet.ooadloophole.controller.interface_model;

import java.util.HashMap;

public class INotification {
    private String subject;
    private HashMap<String, String> templateValues;
    private String receiverId;
    private String type;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<String, String> getTemplateValues() {
        return templateValues;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
