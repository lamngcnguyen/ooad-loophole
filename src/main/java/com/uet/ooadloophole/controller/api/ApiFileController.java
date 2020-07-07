package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.TableDataWrapper;
import com.uet.ooadloophole.model.business.class_elements.TopicSpecFile;
import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import com.uet.ooadloophole.model.business.group_elements.WorkItemFile;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RepoFileService;
import com.uet.ooadloophole.service.business_service.RequirementFileService;
import com.uet.ooadloophole.service.business_service.TopicSpecFileService;
import com.uet.ooadloophole.service.business_service.WorkItemFileService;
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
    private RepoFileService repoFileService;
    @Autowired
    private TopicSpecFileService topicSpecFileService;
    @Autowired
    private RequirementFileService requirementFileService;
    @Autowired
    private WorkItemFileService workItemFileService;

    private Gson gson = new Gson();

    //    ---------------------- Repo files ----------------------
    @RequestMapping(value = "/repo/{type}", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadRepoFile(@RequestParam("file") MultipartFile file, @PathVariable String type, String path, String iterationId) {
        try {
            RepoFile repoFile = repoFileService.upload(file, path, iterationId, type);
            return ResponseEntity.status(HttpStatus.OK).body(repoFile);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/repo", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRepoFile(@RequestParam("file") MultipartFile file, String previousVersionId) {
        try {
            RepoFile newVersion = repoFileService.updateFile(file, previousVersionId);
            return ResponseEntity.status(HttpStatus.OK).body(newVersion);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/repo/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadRepoFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = repoFileService.download(id);
        String timeStamp = repoFileService.getById(id).getFileTimeStamp();
        return getResourceResponseEntity(request, resource, timeStamp);
    }

    @RequestMapping(value = "/repo/{id}/previous", method = RequestMethod.GET)
    public ResponseEntity<String> getPreviousVersions(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(repoFileService.getPreviousVersions(id))));
    }

    @RequestMapping(value = "/repo/{iterationId}/iteration/{type}", method = RequestMethod.GET)
    public ResponseEntity<String> getAllFiles(@PathVariable String iterationId, @PathVariable String type) {
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(repoFileService.getAllByIteration(iterationId, type))));
    }

    @RequestMapping(value = "/repo/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RepoFile> deleteRepoFile(@PathVariable String id) {
        RepoFile repoFile = repoFileService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(repoFile);
    }

    @RequestMapping(value = "/repo/{iterationId}/iteration/deleted", method = RequestMethod.GET)
    public ResponseEntity<String> getAllDeletedFiles(@PathVariable String iterationId) {
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(repoFileService.getDeletedFiles(iterationId))));
    }

    @RequestMapping(value = "/repo/{id}/restore", method = RequestMethod.PUT)
    public ResponseEntity<RepoFile> restoreRepoFile(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(repoFileService.restoreFile(id));
    }

    //    ---------------------- Topic specifications  ----------------------
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
    public ResponseEntity<String> assignTopicIdToSpecFile(String specFileId, @PathVariable String id) {
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
        String timeStamp = topicSpecFileService.findById(id).getFileTimeStamp();
        return getResourceResponseEntity(request, resource, timeStamp);
    }

    @RequestMapping(value = "/spec/topic/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSpecFile(@PathVariable String id) {
        try {
            if (topicSpecFileService.delete(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("deleted");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not deleted");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/topic/multi", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMultiSpecFile(@RequestBody List<String> specFileIds) {
        try {
            List<String> deletedSpecs = new ArrayList<>();
            for (String specFileId : specFileIds) {
                if (topicSpecFileService.delete(specFileId)) {
                    deletedSpecs.add(specFileId);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("deleted" + deletedSpecs)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //    ---------------------- REQUIREMENTS ----------------------
    @RequestMapping(value = "/spec/req", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadRequirementSpecFile(@RequestParam("file") MultipartFile file, String reqId) {
        try {
            RequirementSpecFile requirementSpecFile = requirementFileService.upload(file, reqId);
            return ResponseEntity.status(HttpStatus.OK).body(requirementSpecFile);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @RequestMapping(value = "/spec/req/multi", method = RequestMethod.POST)
//    public ResponseEntity<Object> uploadMultipleRequirementSpecFiles(@RequestParam("files") List<MultipartFile> files) {
//        try {
//            List<RequirementSpecFile> requirementSpecFiles = new ArrayList<>();
//            for (MultipartFile file : files) {
//                requirementSpecFiles.add(requirementFileService.upload(file,));
//            }
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(requirementSpecFiles);
//        } catch (BusinessServiceException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @RequestMapping(value = "/spec/req/assign/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> assignRequirementIdToSpecFile(@RequestParam String specFileId, @PathVariable String id) {
        try {
            requirementFileService.updateRequirementId(specFileId, id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(new ResponseMessage("assigned")));
        } catch (BusinessServiceException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/req/multi/assign/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> assignRequirementIdToMultipleSpecFile(@RequestBody List<String> specFileIds, @PathVariable String id) {
        try {
            for (String specFileId : specFileIds) {
                requirementFileService.updateRequirementId(specFileId, id);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(gson.toJson(new ResponseMessage("assigned")));
        } catch (BusinessServiceException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/spec/req/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> findReqSpecFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = requirementFileService.download(id);
        String timeStamp = requirementFileService.findById(id).getFileTimeStamp();
        return getResourceResponseEntity(request, resource, timeStamp);
    }

    @RequestMapping(value = "/spec/req/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RequirementSpecFile> deleteReqSpecFile(@PathVariable String id) throws IOException, BusinessServiceException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(requirementFileService.deleteFile(id));
        } catch (BusinessServiceException | IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(requirementFileService.deleteFile(id));
        }
    }

    //    ---------------------- Work items ----------------------
    @RequestMapping(value = "/work-item", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadWorkItemFile(@RequestParam("file") MultipartFile file, String workItemId) {
        try {
            WorkItemFile workItemFile = workItemFileService.upload(file, workItemId);
            return ResponseEntity.status(HttpStatus.OK).body(workItemFile);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/work-item/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getWorkItemFile(@PathVariable String id, HttpServletRequest request) {
        Resource resource = workItemFileService.download(id);
        String timeStamp = workItemFileService.getById(id).getFileTimeStamp();
        return getResourceResponseEntity(request, resource, timeStamp);
    }

    @RequestMapping(value = "/work-item/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteWorkItemFile(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(workItemFileService.deleteFile(id));
        } catch (IOException | BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private ResponseEntity<Resource> getResourceResponseEntity(HttpServletRequest request, Resource resource, String timeStamp) {
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
