package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.FileStorageService;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.SpecFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping(value = "/api/file")
public class ApiFileController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private RepoFileService repoFileService;
    @Autowired
    private SpecFileService specFileService;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath, HttpServletRequest request) {
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        String path = filePath.substring(0, filePath.lastIndexOf('/'));

        Resource resource = fileStorageService.loadFileAsResource(fileName, path);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not determine file type!");
        }
        if (contentType == null) contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @RequestMapping(value = "/upload/repo", method = RequestMethod.POST)
    public ResponseEntity uploadRepoFile(@RequestParam("file") MultipartFile file, String path) {
        try {
            repoFileService.upload(file, path);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/upload/spec/", method = RequestMethod.POST)
    public ResponseEntity uploadSpecFile(@RequestParam("file") MultipartFile file, String path, String topicId) {
        try {
            specFileService.upload(file, path, topicId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
