package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.payload.UploadFileResponse;
import com.uet.ooadloophole.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(value = "/group")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createNewGroup(String name, String classId) {
        Group group = new Group();
        group.setGroupName(name);
        group.setClassId(classId);
        groupRepository.save(group);
        return "created";
    }

    @ResponseBody
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public String addStudentToGroup(String studentId, String groupId) {
        Student student = studentRepository.findBy_id(studentId);
        student.setGroupId(groupId);
        studentRepository.save(student);
        return "added";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, String uploadDir) {
        fileStorageService.setPathString(uploadDir);
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize()).toString();
    }

//    @ResponseBody
//    @RequestMapping(value = "/uploadMultipleFiles", method = RequestMethod.POST)
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.stream(files)
//                .map(this::uploadFile)
//                .collect(Collectors.toList());
//    }

    @ResponseBody
    @RequestMapping(value = "/downloadFile/{saveLocation}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String saveLocation, @PathVariable String fileName, HttpServletRequest request) {
        fileStorageService.setPathString(saveLocation);
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
