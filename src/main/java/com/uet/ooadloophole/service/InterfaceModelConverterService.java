package com.uet.ooadloophole.service;

import com.uet.ooadloophole.config.Constant;
import com.uet.ooadloophole.controller.interface_model.DTOClass;
import com.uet.ooadloophole.controller.interface_model.DTOStudent;
import com.uet.ooadloophole.controller.interface_model.IStudent;
import com.uet.ooadloophole.controller.interface_model.IUser;
import com.uet.ooadloophole.model.business.*;
import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.ClassService;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.SemesterService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InterfaceModelConverterService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private SemesterService semesterService;

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

    public DTOStudent convertToDTOStudent(Student student) throws BusinessServiceException {
        DTOStudent dtoStudent = new DTOStudent();
        Class ooadClass = classService.getById(student.getClassId());
        User user = userService.getById(student.getUserId());
        dtoStudent.set_id(student.get_id());
        dtoStudent.setStudentId(student.getStudentId());
        dtoStudent.setClassId(student.getClassId());
        dtoStudent.setClassName(ooadClass.getClassName());
        dtoStudent.setUserId(user.get_id());
        dtoStudent.setFullName(user.getFullName());
        dtoStudent.setEmail(user.getEmail());
        dtoStudent.setActive(user.isActive());
        return dtoStudent;
    }

    public DTOClass convertToDTOClass(Class ooadClass) throws BusinessServiceException {
        DTOClass dtoClass = new DTOClass();
        User user = userService.getById(ooadClass.getTeacherId());
        Semester semester = semesterService.getById(ooadClass.getSemesterId());
        dtoClass.set_id(ooadClass.get_id());
        dtoClass.setClassName(ooadClass.getClassName());
        dtoClass.setTeacherName(user.getFullName());
        dtoClass.setSemesterName(semester.getName());
        dtoClass.setScheduledDayOfWeek(ooadClass.getScheduledDayOfWeek());
        dtoClass.setActive(ooadClass.isActive());
        return dtoClass;
    }
}
