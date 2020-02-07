package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;

import java.util.List;

public interface StudentService {
    Student getById(String studentId) throws BusinessServiceException;

    Student getByStudentId(String studentId) throws BusinessServiceException;

    Student getByUserId(String userId) throws BusinessServiceException;

    List<Student> searchByFullNameOrStudentId(String keyword);

    List<Student> getByClass(String classId);

    List<Student> getByGroup(String groupId);

    Student create(Student student) throws BusinessServiceException;

    List<Student> importStudents(List<Student> students);

    Student update(Student student) throws BusinessServiceException;

    void delete(String studentId) throws BusinessServiceException;
}
