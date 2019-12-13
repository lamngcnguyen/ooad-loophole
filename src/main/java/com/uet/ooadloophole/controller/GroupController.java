package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.*;
import com.uet.ooadloophole.service.FileStorageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/group")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createNewGroup(String name, String classId) {
        Group group = new Group();
        Repository repo = new Repository();
        group.setGroupName(name);
        group.setClassId(classId);
        group.setRepo(repo);
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public ResponseEntity addStudentToGroup(String _id, String groupId) {
        Student student = studentRepository.findBy_id(_id);
        student.setGroupId(groupId);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllStudents/{groupId}", method = RequestMethod.GET)
    public String getAllStudents(@PathVariable String groupId) {
        Gson gson = new Gson();
        List<Student> students = studentRepository.findAllByGroupId(groupId);
        return gson.toJson(students);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        ArrayList<File> repoFiles;
        String userId = secureUserDetailService.getCurrentUser().get_id();
        Student currentStudent = studentRepository.findByUserId(userId);
        Group currentGroup = groupRepository.findBy_id(currentStudent.getGroupId());
        Repository repo = currentGroup.getRepo();
        String groupId = currentStudent.getGroupId();
        String classId = currentStudent.getClassId();
        String saveLocation = "repo/" + classId + "/" + groupId;

        String fileName = fileStorageService.storeFile(file, saveLocation);
        File uploadedFile = new File();
        uploadedFile.setFileName(fileName);
        uploadedFile.setUploaderId(userId);
        if (repo.getFiles() == null) {
            repoFiles = new ArrayList<>();
        } else {
            repoFiles = repo.getFiles();
        }
        repoFiles.add(uploadedFile);
        repo.setFiles(repoFiles);
        currentGroup.setRepo(repo);
        groupRepository.save(currentGroup);
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
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
//        fileStorageService.setPathString(saveLocation);
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, "placeholder");

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
