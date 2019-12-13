package com.uet.ooadloophole.controller;

import com.google.gson.Gson;
import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.TeacherRepository;
import com.uet.ooadloophole.model.*;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    private TeacherRepository teacherRepository;

    @ResponseBody
    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = "application/json")
    public void importClass(@RequestBody Map<String, Object> payload) {
        JSONObject jsonObject = new JSONObject(payload);
        Class ooadClass = new com.uet.ooadloophole.model.Class();
        ooadClass.setClassName((String) jsonObject.get("name"));
        classRepository.save(ooadClass);

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
    @RequestMapping(value = "/addStudent/{classId}", method = RequestMethod.POST)
    public ResponseEntity addStudent(@PathVariable String classId, String studentId) {
        Student student = studentRepository.findBy_id(studentId);
        student.setClassId(classId);
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllGroups/{classId}", method = RequestMethod.GET)
    public String getAllGroups(@PathVariable String classId) {
        Gson gson = new Gson();
        List<Group> groups = groupRepository.findAllByClassId(classId);
        return gson.toJson(groups);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllStudents/{classId}", method = RequestMethod.GET)
    public String getAllStudents(@PathVariable String classId) {
        Gson gson = new Gson();
        List<Student> students = studentRepository.findAllByClassId(classId);
        return gson.toJson(students);
    }
}
