package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.model.business.SpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.SpecFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/files")
public class ApiFileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RepoFileService repoFileService;
    @Autowired
    private SpecFileService specFileService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath, HttpServletRequest request) {
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        String path = filePath.substring(0, filePath.lastIndexOf('/'));

        Resource resource = fileService.loadFileAsResource(fileName, path);
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

    @RequestMapping(value = "/repo", method = RequestMethod.POST)
    public ResponseEntity<String> uploadRepoFile(@RequestParam("file") MultipartFile file, String path) {
        try {
            repoFileService.upload(file, path);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadSpecFile(@RequestParam("file") MultipartFile file, String topicId) {
        try {
            SpecFile specFile = specFileService.upload(file, topicId);
            return ResponseEntity.status(HttpStatus.OK).body(specFile);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/multiple", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadMultipleSpecFiles(@RequestParam("files") List<MultipartFile> files, String topicId) {
        try {
            List<SpecFile> specFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                specFiles.add(specFileService.upload(file, topicId));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(specFiles);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/{id}", method = RequestMethod.GET)
    private ResponseEntity<Resource> downloadSpecFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = specFileService.download(id);
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
}
