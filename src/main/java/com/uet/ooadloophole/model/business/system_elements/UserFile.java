package com.uet.ooadloophole.model.business.system_elements;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

public class UserFile {
    private String fileName;
    private String fileExtension;
    private String fileTimeStamp;
    private LocalDateTime timeStamp;
    @DBRef
    private LoopholeUser uploader;
    private String path;
    private List<String> previousVersionIdList;
    private boolean latestVersion;
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

    public LoopholeUser getUploader() {
        return uploader;
    }

    public void setUploader(LoopholeUser uploader) {
        this.uploader = uploader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getPreviousVersionIdList() {
        return previousVersionIdList;
    }

    public void setPreviousVersionIdList(List<String> previousVersionIdList) {
        this.previousVersionIdList = previousVersionIdList;
    }

    public boolean isLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(boolean latestVersion) {
        this.latestVersion = latestVersion;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
