package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Group;
//import com.uet.ooadloophole.model.Repository;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.UserFile;
import com.uet.ooadloophole.service.business_service.FileStorageService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

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
//        Repository repo = new Repository();
        group.setGroupName(name);
        group.setClassId(classId);
//        group.setRepo(repo);
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/{groupId}/members", method = RequestMethod.POST)
    public ResponseEntity addStudentToGroup(@PathVariable String groupId, String _id) {
        Student student = studentRepository.findBy_id(_id);
        student.setGroupId(groupId);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/{groupId}/students", method = RequestMethod.GET)
    public String getAllStudents(@PathVariable String groupId) {
        Gson gson = new Gson();
        List<Student> students = studentRepository.findAllByGroupId(groupId);
        return gson.toJson(students);
    }

    @ResponseBody
    @RequestMapping(value = "/{groupId}/selectTopic", method = RequestMethod.POST)
    public ResponseEntity selectTopic(@PathVariable String groupId, String topicId) {
        Group group = groupRepository.findBy_id(groupId);
        group.setTopicId(topicId);
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    //========================= File Operations ==========================

    @ResponseBody
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, String path) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    //TODO: className, groupName
    @ResponseBody
    @RequestMapping(value = {"/file/list/{dir}", "/file/list"}, method = RequestMethod.GET, produces = "application/json")
    public String searchDirectory(@CookieValue String groupId, @CookieValue String classId, @PathVariable Optional<String> dir) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String path = "repo/" + classId + "/";

        if (!dir.isPresent()) {
            jsonObject.addProperty("name", groupId);
            path = path + groupId;
        } else {
            jsonObject.addProperty("name", dir.get().substring(dir.get().lastIndexOf("/") + 1));
            path = path + groupId + "/" + dir;
        }
        jsonObject.addProperty("type", "folder");

        File folder = new File(path);
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            JsonObject child = new JsonObject();
            child.addProperty("name", fileEntry.getName());
            child.addProperty("lastModified", sdf.format(fileEntry.lastModified()));
            if (fileEntry.isDirectory()) {
                child.addProperty("type", "folder");
            } else {
                child.addProperty("type", "file");
            }
            jsonArray.add(child);
        }
        jsonObject.add("children", jsonArray);
        return gson.toJson(jsonObject);
    }

    @ResponseBody
    @RequestMapping(value = "/file/multipleUpload", method = RequestMethod.POST)
    public ResponseEntity uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files, String path) {
        for (MultipartFile file : files) {
            uploadFile(file, path);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/file/download/{directory}/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@CookieValue String groupId, @CookieValue String classId, @PathVariable String directory, @PathVariable String fileName, HttpServletRequest request) {
        String path = "repo/" + classId + "/" + groupId + "/" + directory;
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, path);

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

    //======================= Iteration CRUD =======================
    @ResponseBody
    @RequestMapping(value = "/file/create/iteration", method = RequestMethod.POST)
    public ResponseEntity createNewFolder(@CookieValue String groupId, @CookieValue String classId, String iteration) {
        Path newPath = fileStorageService.createPath("repo/" + classId + "/" + groupId + "/code/" + iteration);
        if (newPath != null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Folder not created");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/edit/iteration", method = RequestMethod.PUT)
    public ResponseEntity editIteration(@CookieValue String groupId, @CookieValue String classId, String oldName, String newName) {
        String path = "repo/" + classId + "/" + groupId + "/code/";
        boolean edit = fileStorageService.editFileName(path + oldName, path + newName);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Iteration name not changed");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/delete/iteration", method = RequestMethod.PUT)
    public ResponseEntity deleteIteration(@CookieValue String groupId, @CookieValue String classId, String iteration) throws IOException {
        String path = "repo/" + classId + "/" + groupId + "/code/";
        boolean edit = fileStorageService.deleteFile(path + iteration);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Iteration name not changed");
        }
    }

    //======================= Actor CRUD =======================
    @ResponseBody
    @RequestMapping(value = "/file/create/actor", method = RequestMethod.POST)
    public ResponseEntity createNewActor(@CookieValue String groupId, @CookieValue String classId, String actor) {
        Path newPath = fileStorageService.createPath("repo/" + classId + "/" + groupId + "/docs/" + actor);
        if (newPath != null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Folder not created");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/edit/actor", method = RequestMethod.PUT)
    public ResponseEntity editActor(@CookieValue String groupId, @CookieValue String classId, String oldName, String newName) {
        String path = "repo/" + classId + "/" + groupId + "/docs/";
        boolean edit = fileStorageService.editFileName(path + oldName, path + newName);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Actor name not changed");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/delete/actor", method = RequestMethod.PUT)
    public ResponseEntity deleteActor(@CookieValue String groupId, @CookieValue String classId, String actor) throws IOException {
        String path = "repo/" + classId + "/" + groupId + "/docs/";
        boolean edit = fileStorageService.deleteFile(path + actor);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Iteration name not changed");
        }
    }

    //======================= Use Case CRUD =======================
    @ResponseBody
    @RequestMapping(value = "/file/create/useCase", method = RequestMethod.POST)
    public ResponseEntity createUseCase(@CookieValue String groupId, @CookieValue String classId, String actor, String useCase) {
        Path newPath = fileStorageService.createPath("repo/" + classId + "/" + groupId + "/docs/" + actor + "/" + useCase);
        if (newPath != null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Folder not created");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/edit/useCase", method = RequestMethod.PUT)
    public ResponseEntity editUseCase(@CookieValue String groupId, @CookieValue String classId, String actor, String oldName, String newName) {
        String path = "repo/" + classId + "/" + groupId + "/docs/" + actor + "/";
        boolean edit = fileStorageService.editFileName(path + oldName, path + newName);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Use case name not changed");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/file/delete/useCase", method = RequestMethod.PUT)
    public ResponseEntity deleteUseCase(@CookieValue String groupId, @CookieValue String classId, String actor, String useCase) throws IOException {
        String path = "repo/" + classId + "/" + groupId + "/docs/" + actor + "/";
        boolean edit = fileStorageService.deleteFile(path + useCase);
        if (edit) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Iteration name not changed");
        }
    }
}
