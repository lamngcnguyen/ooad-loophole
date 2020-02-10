package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.Student;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {
    List<Class> getByTeacherId(String teacherId) throws BusinessServiceException;

    Class getById(String id) throws BusinessServiceException;

    Class create(Class ooadClass);

    Class create(String name, String teacherId, String semesterId, int scheduledDayOfWeek);

    void delete(String classId) throws BusinessServiceException;

    Class update(Class ooadClass) throws BusinessServiceException;

    List<Student> importStudents(String classId, List<Student> payload);
}
