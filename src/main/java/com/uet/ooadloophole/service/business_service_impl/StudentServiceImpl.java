package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.UserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Override
    public Student getStudentById(String id) throws BusinessServiceException {
        Student result = studentRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No student found with this id");
        }
        return result;
    }

    @Override
    public Student getStudentByStudentId(String studentId) throws BusinessServiceException {
        Student result = studentRepository.findByStudentId(studentId);
        if (result == null) {
            throw new BusinessServiceException("No student found with this student id");
        }
        return result;
    }

    @Override
    public Student getStudentByUserId(String userId) throws BusinessServiceException {
        Student result = studentRepository.findByUserId(userId);
        if (result == null) {
            throw new BusinessServiceException("No student found with this user id");
        }
        return result;
    }

    @Override
    public List<Student> searchStudentByFullName(String keyword) {
        return studentRepository.findAllByFullNameLikeIgnoreCase(keyword);
    }

    @Override
    public List<Student> getStudentByClass(String classId) {
        return studentRepository.findAllByClassId(classId);
    }

    @Override
    public List<Student> getStudentByGroup(String groupId) {
        return studentRepository.findAllByGroupId(groupId);
    }

    @Override
    public Student createStudent(Student student) throws BusinessServiceException {
        return null;
    }

    @Override
    public Student updateStudent(Student student) throws BusinessServiceException {
        return null;
    }

    @Override
    public void deleteStudent(String studentId) throws BusinessServiceException {

    }
}
