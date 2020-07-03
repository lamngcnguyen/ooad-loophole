package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    List<Student> getAll();

    Student getById(String studentId) throws BusinessServiceException;

    Student getByStudentId(String studentId) throws BusinessServiceException;

    List<Student> getByGroupIdExcludingLeader(String leaderId, String groupId);

    Student getByUserId(String userId) throws BusinessServiceException;

    List<Student> searchByFullNameOrStudentId(String keyword);

    List<Student> getByClass(String classId);

    List<Student> getByGroup(String groupId);

    List<Student> getStudentsWithoutGroup(String classId);

    Student create(Student student) throws BusinessServiceException;

    boolean studentIdExists(String studentId);

    List<Student> importStudents(List<Student> students);

    Student update(String id, Student student) throws BusinessServiceException;

    void assignGroup(Student student, String groupId) throws BusinessServiceException;

    void delete(String studentId) throws BusinessServiceException;

    int countByClassId(String classId);
}
