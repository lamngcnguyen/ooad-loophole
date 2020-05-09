package com.uet.ooadloophole.model.business;

import java.time.LocalDateTime;

public class UserFile {
    private String fileName;
    private String fileExtension;
    private String fileTimeStamp;
    private LocalDateTime timeStamp;
    private String uploaderId;
    private String path;
    private String previousVersionId;
    private boolean deleted;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileTimeStamp() {
        return fileTimeStamp;
    }

    public void setFileTimeStamp(String fileTimeStamp) {
        this.fileTimeStamp = fileTimeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(String previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
