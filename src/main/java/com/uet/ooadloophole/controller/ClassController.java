package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.ClassRepository;
import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.service.SecureUserDetailService;
import com.uet.ooadloophole.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = "application/json")
    public void importClass(@RequestBody Map<String, Object> payload){
        JSONObject jsonObject = new JSONObject(payload);
        Class ooadClass = new com.uet.ooadloophole.model.Class();
        ooadClass.setClassName((String) jsonObject.get("name"));
        classRepository.save(ooadClass);

        JSONArray array = jsonObject.getJSONArray("students");
        for (Object o : array){
            JSONObject object = (JSONObject) o;
            if(studentRepository.findByStudentId((String) object.get("studentId")) == null) {
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
    public String createClass(String name, String startDate, String endDate){
        String teacherId = secureUserDetailService.getCurrentUser().get_id();
        Class ooadClass = new com.uet.ooadloophole.model.Class();
        ooadClass.setClassName(name);
        ooadClass.setStartDate(startDate);
        ooadClass.setEndDate(endDate);
        ooadClass.setTeacherId(teacherId);
        classRepository.save(ooadClass);
        return "created";
    }
}
