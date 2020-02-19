package com.uet.ooadloophole.service;

import com.uet.ooadloophole.config.Constant;
import com.uet.ooadloophole.controller.interface_model.IStudent;
import com.uet.ooadloophole.controller.interface_model.IUser;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InterfaceModelConverterService {
    @Autowired
    private RoleService roleService;

    public User convertUserInterface(IUser iUser) throws BusinessServiceException {
        Set<Role> roles = new HashSet<>();
        User user = new User();
        user.setUsername(iUser.getUsername());
        user.setFullName(iUser.getFullName());
        user.setEmail(iUser.getEmail());
        if (iUser.isAdmin()) {
            Role role = roleService.getByName(Constant.ROLE_ADMIN);
            roles.add(role);
        }
        if (iUser.isTeacher()) {
            Role role = roleService.getByName(Constant.ROLE_TEACHER);
            roles.add(role);
        }
        user.setRoles(roles);
        return user;
    }

    public Student convertStudentInterface(IStudent iStudent) {
        Student student = new Student();
        student.setFullName(iStudent.getFullName());
        student.setStudentId(iStudent.getStudentId());
        student.setClassId(iStudent.getClassId());
        return student;
    }
}
