package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {
    List<Class> getAll();

    List<Class> getByTeacherId(String teacherId) throws BusinessServiceException;

    List<Class> getByTeacherIdAndSemesterId(String teacherId, String semesterId);

    Class getById(String id) throws BusinessServiceException;

    Class getByTeacherIdAndClassName(String teacherId, String name) throws BusinessServiceException;

    Class create(Class ooadClass);

    List<Student> getAllStudents(String classId);

    List<Student> getStudentsWithoutGroup(String classId);

    void delete(String classId) throws BusinessServiceException;

    Class update(String id, Class ooadClass) throws BusinessServiceException;

    List<Student> importStudents(String classId, List<Student> payload);

    List<Class> searchByName(String className);

    boolean classNameExists(String teacherId, String className);
}
