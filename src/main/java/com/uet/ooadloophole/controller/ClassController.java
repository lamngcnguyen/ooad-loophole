package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.database.*;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.*;
import com.uet.ooadloophole.service.FileStorageService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Controller
@RequestMapping(value = "/class")
public class ClassController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecureUserDetailService secureUserDetailService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private DeadlineRepository deadlineRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @ResponseBody
    @RequestMapping(value = "/students/import", method = RequestMethod.POST, consumes = "application/json")
    public void importClass(@RequestBody Map<String, Object> payload) {
        JSONObject jsonObject = new JSONObject(payload);
        Class ooadClass;
        if (classRepository.findClassByClassName((String) jsonObject.get("name")) == null) {
            ooadClass = new Class();
            ooadClass.setClassName((String) jsonObject.get("name"));
            classRepository.save(ooadClass);
        } else {
            ooadClass = classRepository.findClassByClassName((String) jsonObject.get("name"));
        }

        JSONArray array = jsonObject.getJSONArray("students");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (studentRepository.findByStudentId((String) object.get("studentId")) == null) {
                User user = new User();
                Student student = new Student();
                Group group = new Group();

                user.setFullName((String) object.get("fullName"));
                user.setPassword((String) object.get("studentId"));
                user.setEmail((String) object.get("email"));
                userService.saveUser(user, "USER");

                if (groupRepository.findByGroupName((String) object.get("groupName")) == null) {
                    group.setGroupName((String) object.get("groupName"));
                    group.setClassId(ooadClass.get_id());
                    groupRepository.save(group);
                } else {
                    group = groupRepository.findByGroupName((String) object.get("groupName"));
                }

                student.setUserId(user.get_id());
                student.setClassId(ooadClass.get_id());
                student.setGroupId(group.get_id());
                student.setStudentId((String) object.get("studentId"));
                studentRepository.save(student);
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createClass(String name, String startDate, String endDate) {
        if(secureUserDetailService.isTeacher()) {
            Class ooadClass = new com.uet.ooadloophole.model.Class();
            ooadClass.setClassName(name);
            ooadClass.setStartDate(startDate);
            ooadClass.setEndDate(endDate);
            ooadClass.setTeacherId(secureUserDetailService.getTeacherId());
            classRepository.save(ooadClass);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/add/students", method = RequestMethod.POST)
    public ResponseEntity addStudent(@PathVariable String classId, String studentId) {
        Student student = studentRepository.findBy_id(studentId);
        student.setClassId(classId);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/groups", method = RequestMethod.GET)
    public String getAllGroups(@PathVariable String classId) {
        Gson gson = new Gson();
        List<Group> groups = groupRepository.findAllByClassId(classId);
        return gson.toJson(groups);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/students", method = RequestMethod.GET)
    public String getAllStudents(@PathVariable String classId) {
        Gson gson = new Gson();
        List<Student> students = studentRepository.findAllByClassId(classId);
        return gson.toJson(students);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/add/deadlines", method = RequestMethod.POST)
    public ResponseEntity addDeadline(@PathVariable String classId, String deadlineTime, String message) {
        Deadline deadline = new Deadline();
        deadline.setClassId(classId);
        deadline.setDate(deadlineTime);
        deadline.setMessage(message);
        deadlineRepository.save(deadline);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/deadlines", method = RequestMethod.GET)
    public String getDeadlines(@PathVariable String classId) {
        List<Deadline> deadlineList = deadlineRepository.findAllByClassId(classId);
        return new Gson().toJson(deadlineList);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/topics/create", method = RequestMethod.POST)
    public ResponseEntity createNewTopic(@PathVariable String classId, String topicName, String groupId) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setClassId(classId);
        topicRepository.save(topic);
        if(groupId != null) {
            Group group = groupRepository.findBy_id(groupId);
            group.setTopicId(topic.get_id());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/topics/{topicId}/edit/name", method = RequestMethod.POST)
    public ResponseEntity editTopicName(@PathVariable String topicId, String topicName) {
        Topic topic = topicRepository.findBy_id(topicId);
        topic.setName(topicName);
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/topics/{topicId}/edit/details", method = RequestMethod.POST)
    public ResponseEntity editTopicDetails(@PathVariable String topicId, String topicDetails) {
        Topic topic = topicRepository.findBy_id(topicId);
        topic.setDetails(topicDetails);
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/topics", method = RequestMethod.GET)
    public String getAllTopics(@PathVariable String classId) {
        List<Topic> topics = topicRepository.findAllByClassId(classId);
        return new Gson().toJson(topics);
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/topics/{topicId}/upload", method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String topicId, @PathVariable String classId) {
        Topic topic = topicRepository.findBy_id(topicId);
        ArrayList<UserFile> specificationList = topic.getSpecificationFiles();
        String saveLocation = "repo/" + classId + "/topic/" + topicId;
        UserFile uploadedUserFile = new UserFile();
        String userId = secureUserDetailService.getCurrentUser().get_id();

        String fileName = fileStorageService.storeFile(file, saveLocation);
        uploadedUserFile.setFileName(fileName);
        uploadedUserFile.setUploaderId(userId);

        specificationList.add(uploadedUserFile);
        topic.setSpecificationFiles(specificationList);
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }
}
