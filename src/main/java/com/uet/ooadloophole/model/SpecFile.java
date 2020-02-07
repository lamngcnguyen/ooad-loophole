package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

public class SpecFile extends UserFile {
    @Id
    private String _id;
    private String fileId;
    private String topicId;

    @Override
    public String get_id() {
        return _id;
    }

    @Override
    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
