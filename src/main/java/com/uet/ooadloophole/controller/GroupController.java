package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/group")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;

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


}
