package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.model.business.TopicSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.FileService;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.TopicSpecFileService;
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
import java.util.Objects;


@RestController
@RequestMapping(value = "/api/files")
public class ApiFileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private RepoFileService repoFileService;
    @Autowired
    private TopicSpecFileService topicSpecFileService;

    private Gson gson = new Gson();

    //TODO: fix this
    @RequestMapping(value = "/{filePath}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String filePath, HttpServletRequest request) {
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

    @RequestMapping(value = "/spec/topic", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadTopicSpecFile(@RequestParam("file") MultipartFile file) {
        try {
            TopicSpecFile topicSpecFile = topicSpecFileService.upload(file);
            return ResponseEntity.status(HttpStatus.OK).body(topicSpecFile);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/topic/multi", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadMultipleTopicSpecFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<TopicSpecFile> topicSpecFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                topicSpecFiles.add(topicSpecFileService.upload(file));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(topicSpecFiles);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/topic/assign/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> assignTopicIdToSpecFile(@RequestBody String specFileId, @PathVariable String id) {
        try {
            topicSpecFileService.updateTopicId(specFileId, id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(new ResponseMessage("assigned")));
        } catch (BusinessServiceException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/topic/multi/assign/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> assignTopicIdToMultipleSpecFile(@RequestBody List<String> specFileIds, @PathVariable String id) {
        try {
            for (String specFileId : specFileIds) {
                topicSpecFileService.updateTopicId(specFileId, id);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(new ResponseMessage("assigned")));
        } catch (BusinessServiceException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/topic/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadSpecFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = topicSpecFileService.download(id);
        String timeStamp = topicSpecFileService.findById(id).getTimeStamp();

        String contentType = null;
        String fileName = Objects.requireNonNull(resource.getFilename()).replace("_" + timeStamp, "");
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not determine file type!");
        }
        if (contentType == null) contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
