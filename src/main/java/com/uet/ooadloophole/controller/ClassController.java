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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createClass(String name, String startDate, String endDate) {
        if (secureUserDetailService.isTeacher()) {
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
    @RequestMapping(value = "/students/import", method = RequestMethod.POST, consumes = "application/json")
    public void importClass(@CookieValue String classId, @RequestBody Map<String, Object> payload) throws Exception {
        JSONObject jsonObject = new JSONObject(payload);
        Class ooadClass = classRepository.findBy_id(classId);
        if (ooadClass == null) throw new Exception("Class not found!");
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
    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public ResponseEntity addStudent(@CookieValue String classId, String fullName, String studentId, String email, String groupName) {
        if (studentRepository.findBy_id(studentId) != null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        User user = new User();
        Group group = new Group();
        Student student = new Student();
        user.setFullName(fullName);
        user.setPassword(studentId);
        user.setEmail(email);
        userService.saveUser(user, "USER");
        if (groupRepository.findByGroupName(groupName) == null) {
            group.setGroupName(groupName);
            group.setClassId(classId);
            groupRepository.save(group);
        } else {
            group = groupRepository.findByGroupName(groupName);
        }
        student.setUserId(user.get_id());
        student.setClassId(classId);
        student.setGroupId(group.get_id());
        student.setStudentId(studentId);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new StudentDTO(user.get_id(), fullName, email,
                        studentId, group.get_id(), groupName));
    }

    @ResponseBody
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public List<StudentDTO> getAllStudents(@CookieValue("classId") String classId) {
        List<StudentDTO> students = new ArrayList<>();
        studentRepository.findAllByClassId(classId).forEach(student -> {
            String groupName = groupRepository.findBy_id(student.getGroupId()).getGroupName();
            User user = userRepository.findBy_id(student.getUserId());
            students.add(new StudentDTO(user.get_id(), user.getFullName(), user.getEmail(),
                    student.getStudentId(), student.getGroupId(), groupName));
        });
        return students;
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/groups", method = RequestMethod.GET)
    public List<Group> getAllGroups(@PathVariable String classId) {
        List<Group> groups = groupRepository.findAllByClassId(classId);
        return groups;
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/deadlines", method = RequestMethod.POST)
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
    public List<Deadline> getDeadlines(@PathVariable String classId) {
        List<Deadline> deadlineList = deadlineRepository.findAllByClassId(classId);
        return deadlineList;
    }

    @ResponseBody
    @RequestMapping(value = "/{classId}/topics", method = RequestMethod.POST)
    public ResponseEntity createNewTopic(@PathVariable String classId, String topicName, String groupId) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setClassId(classId);
        topicRepository.save(topic);
        if (groupId != null) {
            Group group = groupRepository.findBy_id(groupId);
            group.setTopicId(topic.get_id());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/topics/{topicId}", method = RequestMethod.PUT)
    public ResponseEntity updateTopic(@PathVariable String topicId, Optional<String> topicName, Optional<String> details) {
        Topic topic = topicRepository.findBy_id(topicId);
        if (topicName.isPresent()) topic.setName(topicName.get());
        if (details.isPresent()) topic.setDetails(details.get());
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

    @ResponseBody
    @RequestMapping(value = "/my-class", method = RequestMethod.GET)
    public List<ClassDTO> getClassByTeacherId(@CookieValue("teacherId") String teacherId, HttpServletResponse response) {
        List<ClassDTO> myClasses = new ArrayList<>();
        classRepository.findAllByTeacherId(teacherId).forEach(c ->
                myClasses.add(new ClassDTO(c, studentRepository.findAllByClassId(c.get_id()).size())));
        return myClasses;
    }
}
