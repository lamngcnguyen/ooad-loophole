package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.database.GroupRepository;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.User;
import com.uet.ooadloophole.service.SecureUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecureUserDetailService userDetailService;

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public ModelAndView topicView(@CookieValue String groupId, @CookieValue String userId) {
        User user = userDetailService.getCurrentUser();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/student/topic");
        modelAndView.addObject("userFullName", user.getFullName());
        return modelAndView;
    }

    @RequestMapping(value = "/repo", method = RequestMethod.GET)
    public ModelAndView repoView(@CookieValue String groupId, @CookieValue String userId) {
        Group group = groupRepository.findBy_id(groupId);
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (group.getTopicId() == null) modelAndView.setViewName("/student/unassigned");
            else {
                modelAndView.setViewName("/student/repo");
                modelAndView.addObject("groupName", group.getGroupName());
            }
        } catch (NullPointerException e) {
            //TODO: make a no group assigned page (choose a group)
            modelAndView.setViewName("/student/unassigned");
        }
        User user = userDetailService.getCurrentUser();
        modelAndView.addObject("userFullName", user.getFullName());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/{_id}", method = RequestMethod.PUT)
    public ResponseEntity editStudent(@CookieValue String classId, @PathVariable String _id, Optional<String> fullName, Optional<String> studentId, Optional<String> email, Optional<String> groupName) {
        Student student = studentRepository.findBy_id(_id);
        User user = userRepository.findBy_id(student.getUserId());

        System.out.println("classId: " + classId);
        System.out.println("_id: " + _id);

        if (fullName.isPresent()) {
            user.setFullName(fullName.get());
            userRepository.save(user);
        }
        if (studentId.isPresent()) {
            student.setStudentId(studentId.get());
            user.setPassword(studentId.get());
            studentRepository.save(student);
        }
        if (email.isPresent()) {
            user.setEmail(email.get());
            userRepository.save(user);
        }
        if (groupName.isPresent()) {
            Group group = new Group();
            if (groupRepository.findByGroupName(groupName.get()) == null) {
                group.setGroupName(groupName.get());
                group.setClassId(classId);
                groupRepository.save(group);
                student.setGroupId(group.get_id());
            } else {
                group = groupRepository.findByGroupName(groupName.get());
                student.setGroupId(group.get_id());
            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }
}
