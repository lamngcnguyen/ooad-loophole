package com.uet.ooadloophole.service;

import com.uet.ooadloophole.database.StudentRepository;
import com.uet.ooadloophole.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student getStudentById(String studentId) throws BusinessServiceException {
        Student result = studentRepository.findByStudentId(studentId);
        if(result == null) {
            throw new BusinessServiceException("");
        }
        return result;
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return null;
    }

    @Override
    public Student getStudentByUserId(String userId) {
        return null;
    }

    @Override
    public List<Student> searchStudent(String keyword) {
        return null;
    }

    @Override
    public List<Student> getStudentByClass(String classId) {
        return null;
    }

    @Override
    public List<Student> getStudentByGroup(String groupId) {
        return null;
    }
}
