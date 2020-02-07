package com.uet.ooadloophole.model;

import org.springframework.data.annotation.Id;

public class RepoFile extends UserFile {
    @Id
    private String _id;
    private String fileId;
    private int score;
    private String groupId;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
