package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Class;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {
    List<Class> getByTeacherId(String teacherId) throws BusinessServiceException;

    Class getById(String id) throws BusinessServiceException;

    Class create(Class ooadClass);

    Class create(String name, String teacherId, String semesterId, int scheduledDayOfWeek);

    List<Class> getAll();

    List<Student> getAllStudents(String classId);

    void delete(String classId) throws BusinessServiceException;

    Class update(Class ooadClass) throws BusinessServiceException;

    List<Student> importStudents(String classId, List<Student> payload);

    List<Class> searchByName(String className);
}
