package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constant;
import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.database.UserRepository;
import com.uet.ooadloophole.model.business.Role;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RoleService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public Student getById(String id) throws BusinessServiceException {
        Student result = studentRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No student found with this id");
        }
        return result;
    }

    @Override
    public Student getByStudentId(String studentId) throws BusinessServiceException {
        Student result = studentRepository.findByStudentId(studentId);
        if (result == null) {
            throw new BusinessServiceException("No student found with this student id");
        }
        return result;
    }

    @Override
    public Student getByUserId(String userId) throws BusinessServiceException {
        Student result = studentRepository.findByUserId(userId);
        if (result == null) {
            throw new BusinessServiceException("No student found with this user id");
        }
        return result;
    }

    @Override
    public List<Student> searchByFullNameOrStudentId(String keyword) {
        return studentRepository.findAllByFullNameLikeIgnoreCaseOrStudentIdLike(keyword, keyword);
    }

    @Override
    public List<Student> getByClass(String classId) {
        return studentRepository.findAllByClassId(classId);
    }

    @Override
    public List<Student> getByGroup(String groupId) {
        return studentRepository.findAllByGroupId(groupId);
    }

    @Override
    public Student create(Student student) throws BusinessServiceException {
        try {
            if (studentIdExists(student.getStudentId())) {
                throw new BusinessServiceException("Student ID already exists");
            }
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getByName(Constant.ROLE_STUDENT));

            User user = new User();
            user.setEmail(student.getStudentId() + Constant.EMAIL_SUFFIX);
            user.setUsername(student.getStudentId());
            user.setFullName(student.getFullName());
            user.setPassword(student.getStudentId());
            user.setRoles(roles);
            user = userService.create(user);
            student.setUserId(user.get_id());
            studentRepository.save(student);
            return student;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to create student: " + e.getMessage());
        }
    }

    private boolean studentIdExists(String studentId) {
        return studentRepository.findByStudentId(studentId) != null;
    }

    private boolean studentIdExists(String id, String studentId) {
        return studentRepository.findBy_idNotAndStudentId(id, studentId) != null;
    }

    @Override
    public List<Student> importStudents(List<Student> students) {
        students.forEach(student -> {
            try {
                create(student);
            } catch (BusinessServiceException e) {
                students.remove(student);
            }
        });
        return students;
    }

    @Override
    public Student update(String id, Student student) throws BusinessServiceException {
        try {
            Student dbStudent = getById(id);
            User dbUser = userService.getById(dbStudent.getUserId());
            dbUser.setEmail(student.getStudentId() + Constant.EMAIL_SUFFIX);
            dbUser.setFullName(student.getFullName());
            dbUser.setUsername(student.getStudentId());
            dbStudent.setGroupId(student.getGroupId());
            dbStudent.setStudentId(student.getStudentId());
            dbStudent.setFullName(student.getFullName());

            userRepository.save(dbUser);
            studentRepository.save(dbStudent);
            return dbStudent;
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to update student: " + e.getMessage());
        }
    }

    @Override
    public void delete(String studentId) throws BusinessServiceException {
        try {
            Student dbStudent = getById(studentId);
            studentRepository.delete(dbStudent);
            userService.delete(dbStudent.getUserId());
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete student: " + e.getMessage());
        }
    }
}
