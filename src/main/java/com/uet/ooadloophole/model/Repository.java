package com.uet.ooadloophole.model;

import java.util.ArrayList;

public class Repository {
    private ArrayList<UserFile> userFiles;

    public ArrayList<UserFile> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(ArrayList<UserFile> userFiles) {
        this.userFiles = userFiles;
    }
}
