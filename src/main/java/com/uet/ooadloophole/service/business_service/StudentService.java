package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface StudentService {
    Student getStudentById(String studentId) throws BusinessServiceException;

    Student getStudentByStudentId(String studentId) throws BusinessServiceException;

    Student getStudentByUserId(String userId) throws BusinessServiceException;

    List<Student> searchStudentByFullName(String keyword);

    List<Student> getStudentByClass(String classId);

    List<Student> getStudentByGroup(String groupId);

    Student createStudent(Student student) throws BusinessServiceException;

    Student updateStudent(Student student) throws BusinessServiceException;

    void deleteStudent(String studentId) throws BusinessServiceException;
}
