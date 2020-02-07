package com.uet.ooadloophole.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GroupRepoService {
    @Autowired
    private FileService fileService;
    @Value("${repo.dir}")
    private String REPO_DIR;
    @Value("${group.dir}")
    private String GROUP_DIR;

    public void initializeRepo(String groupId, String classId) {
        String groupDir = REPO_DIR + "/" + classId + "/" + GROUP_DIR + "/" + groupId;
        fileService.createPath(groupDir + "/Docs");
        fileService.createPath(groupDir + "/Source");
    }
}
