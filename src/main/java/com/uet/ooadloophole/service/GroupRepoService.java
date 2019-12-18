package com.uet.ooadloophole.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GroupRepoService {
    @Autowired
    private FileStorageService fileStorageService;
    @Value("${repo.dir}")
    private String REPO_DIR;
    @Value("${group.dir}")
    private String GROUP_DIR;

    public void initializeRepo(String groupId, String classId) {
        String groupDir = REPO_DIR + "/" + classId + "/" + GROUP_DIR + "/" + groupId;
        fileStorageService.createPath(groupDir + "/Docs");
        fileStorageService.createPath(groupDir + "/Source");
    }
}
