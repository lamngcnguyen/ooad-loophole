package com.uet.ooadloophole.model.business.grading_elements;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Question {
    @Id
    private String _id;
    private String question;
    private String templateId;
    private List<Answer> answers;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
