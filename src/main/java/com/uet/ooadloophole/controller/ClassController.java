package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.*;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.*;
import com.uet.ooadloophole.model.dto.ClassDTO;
import com.uet.ooadloophole.model.dto.IterationDTO;
import com.uet.ooadloophole.model.dto.StudentDTO;
import com.uet.ooadloophole.model.dto.TopicDTO;
import com.uet.ooadloophole.service.FileStorageService;
import com.uet.ooadloophole.service.GroupRepoService;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.business_service_impl.UserServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ALL")
@Controller
@RequestMapping(value = "/class")
public class ClassController {
    @Autowired
    private UserServiceImpl userServiceImpl;
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
    private FileStorageService fileStorageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IterationRepository iterationRepository;
    @Autowired
    private UserFileRepository userFileRepository;
    @Autowired
    private GroupRepoService groupRepoService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createClass(String name, String startDate, String endDate, int dayOfWeek) throws ParseException {
        if (secureUserDetailService.isTeacher()) {
            Class ooadClass = new com.uet.ooadloophole.model.Class();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate myStartDate = LocalDate.parse(startDate, dateTimeFormatter);
            LocalDate myEndDate = LocalDate.parse(endDate, dateTimeFormatter);
            ooadClass.setClassName(name);
            ooadClass.setStartDate(myStartDate);
            ooadClass.setEndDate(myEndDate);
            ooadClass.setScheduledDayOfWeek(dayOfWeek);
            ooadClass.setTeacherId(secureUserDetailService.getTeacherId());
            classRepository.save(ooadClass);

            LocalDateTime startDateTime = myStartDate.atStartOfDay();
            while (!myStartDate.isAfter(myEndDate)) {
                if (myStartDate.getDayOfWeek().getValue() == dayOfWeek - 1) {
                    Iteration iteration = new Iteration();
                    iteration.setClassId(ooadClass.get_id());
                    iteration.setStartDateTime(startDateTime);
                    iteration.setEndDateTime(myStartDate.atStartOfDay().minusMinutes(1));
                    iterationRepository.save(iteration);

                    startDateTime = myStartDate.atStartOfDay();
                }
                myStartDate = myStartDate.plusDays(1);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity deleteClass(@CookieValue String classId) {
        try {
            classRepository.deleteById(classId);

            List<Student> students = studentRepository.deleteAllByClassId(classId);
            students.forEach(student -> userRepository.deleteById(student.getUserId()));

            groupRepository.deleteAllByClassId(classId);

            List<Topic> topics = topicRepository.deleteAllByClassId(classId);
            topics.forEach(topic -> {
                topic.getSpecificationFiles().forEach(userFile -> {
                    userFileRepository.deleteById(userFile.get_id());
                });
            });

            iterationRepository.deleteAllByClassId(classId);
            fileStorageService.deleteDirectory("repo/" + classId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/students/import", method = RequestMethod.POST, consumes = "application/json")
    public void importStudents(@CookieValue String classId, @RequestBody Map<String, Object> payload) throws Exception {
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
                userServiceImpl.saveUser(user, "USER");

                if (groupRepository.findByClassIdAndGroupName(classId, (String) object.get("groupName")) == null) {
                    group.setGroupName((String) object.get("groupName"));
                    group.setClassId(ooadClass.get_id());
                    groupRepository.save(group);
                    groupRepoService.initializeRepo(group.get_id(), classId);
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
        userServiceImpl.saveUser(user, "USER");
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
                .body(new StudentDTO(student.get_id(), fullName, email,
                        studentId, group.get_id(), groupName));
    }

    @ResponseBody
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public List<StudentDTO> getAllStudents(@CookieValue("classId") String classId) {
        List<StudentDTO> students = new ArrayList<>();
        studentRepository.findAllByClassId(classId).forEach(student -> {
            String groupName = groupRepository.findBy_id(student.getGroupId()).getGroupName();
            User user = userRepository.findBy_id(student.getUserId());
            students.add(new StudentDTO(student.get_id(), user.getFullName(), user.getEmail(),
                    student.getStudentId(), student.getGroupId(), groupName));
        });
        return students;
    }

    @ResponseBody
    @RequestMapping(value = "/unassigned-groups", method = RequestMethod.GET)
    public List<Group> getUnassignedGroups(@CookieValue String classId) {
        List<Group> groups = groupRepository.findAllByClassIdAndTopicIdIsNull(classId);
        return groups;
    }

    @ResponseBody
    @RequestMapping(value = "/iterations", method = RequestMethod.GET)
    public List<IterationDTO> getIterations(@CookieValue String classId) {
        List<IterationDTO> iterations = new ArrayList<>();
        iterationRepository.findAllByClassId(classId).forEach((i) -> {
            iterations.add(new IterationDTO(i));
        });
        return iterations;
    }

    @ResponseBody
    @RequestMapping(value = "/topics", method = RequestMethod.POST)
    public ResponseEntity createNewTopic(@CookieValue String classId, String topicName, Optional<String> description, Optional<String> groupId,
                                         Optional<MultipartFile[]> files) {
        Topic topic = new Topic();

        topic.setName(topicName);
        topic.setClassId(classId);
        if (description.isPresent())
            topic.setDescriptions(description.get());
        topicRepository.save(topic);

        String groupName = "";
        if (groupId.isPresent()) {
            Group group = groupRepository.findBy_id(groupId.get());
            group.setTopicId(topic.get_id());
            groupRepository.save(group);
            groupName = group.getGroupName();
        }

        if (files.isPresent()) {
            String userId = secureUserDetailService.getCurrentUser().get_id();
            List<UserFile> specFiles = new ArrayList<>();
            for (MultipartFile file : files.get()) {
                String saveLocation = "repo/" + classId + "/topic/" + topic.get_id();
                UserFile newFile = new UserFile();
                String fileName = fileStorageService.storeFile(file, saveLocation);
                newFile.setFileName(fileName);
                newFile.setUploaderId(userId);
                newFile.setTimeStamp(LocalDateTime.now());
                newFile.setPath(saveLocation + '/' + fileName);
                newFile.setScore(0);
                userFileRepository.save(newFile);
                specFiles.add(newFile);
            }
            topic.setSpecificationFiles(specFiles);
            topicRepository.save(topic);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new TopicDTO(topic,
                (groupId.isPresent()) ? groupId.get() : "", groupName));
    }

    @ResponseBody
    @RequestMapping(value = "/topics/{topicId}", method = RequestMethod.PUT)
    public ResponseEntity updateTopic(@PathVariable String topicId, Optional<String> topicName, Optional<String> description) {
        Topic topic = topicRepository.findBy_id(topicId);
        if (topicName.isPresent()) topic.setName(topicName.get());
        if (description.isPresent()) topic.setDescriptions(description.get());
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    public List<TopicDTO> getAllTopics(@CookieValue String classId) {
        List<TopicDTO> topics = new ArrayList<>();
        topicRepository.findAllByClassId(classId).forEach(topic -> {
            Group assignedGroup = groupRepository.findByTopicId(topic.get_id());
            String groupId = (assignedGroup == null) ? "0" : assignedGroup.get_id();
            String groupName = (assignedGroup == null) ? "Unset" : assignedGroup.getGroupName();
            topics.add(new TopicDTO(topic, groupId, groupName));
        });
        return topics;
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
