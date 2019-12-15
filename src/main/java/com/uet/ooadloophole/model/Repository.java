package com.uet.ooadloophole.model;

import java.util.ArrayList;

public class Repository {
    //TODO: Iteration to review, linking iteration to repo
    private ArrayList<UserFile> userFiles;
    private ArrayList<String> filesToReview;

    public ArrayList<UserFile> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(ArrayList<UserFile> userFiles) {
        this.userFiles = userFiles;
    }

    public ArrayList<String> getFilesToReview() {
        return filesToReview;
    }

    public void setFilesToReview(ArrayList<String> filesToReview) {
        this.filesToReview = filesToReview;
    }
}
