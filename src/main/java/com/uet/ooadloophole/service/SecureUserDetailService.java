package com.uet.ooadloophole.service;

import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.TeacherRepository;
import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service

public class SecureUserDetailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    private User getCurrentSecureUser() throws ClassCastException {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUsername() {
        User loggedInUser = getCurrentSecureUser();
        return loggedInUser.getUsername();
    }

    public com.uet.ooadloophole.model.User getCurrentUser() {
        com.uet.ooadloophole.model.User user = userRepository.findByEmail(getUsername());
        return user;
    }

    public boolean isTeacher() {
        return teacherRepository.findByUserId(getCurrentUser().get_id()) != null;
    }

    public String getTeacherId() {
        return teacherRepository.findByUserId(getCurrentUser().get_id()).get_id();
    }

    public boolean isStudent() {
        return studentRepository.findByUserId(getCurrentUser().get_id()) != null;
    }

    public String getStudentId() {
        return studentRepository.findByUserId(getCurrentUser().get_id()).get_id();
    }
}
