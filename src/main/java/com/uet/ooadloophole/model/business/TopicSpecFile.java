package com.uet.ooadloophole.model.business;

import org.springframework.data.annotation.Id;

public class TopicSpecFile extends UserFile {
    @Id
    private String _id;
    private String topicId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
