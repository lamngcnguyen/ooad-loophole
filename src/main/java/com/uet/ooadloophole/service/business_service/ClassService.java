package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Class;
import com.uet.ooadloophole.model.interface_model.IClass;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ClassService {
    Class getById(String id) throws BusinessServiceException;

    Class create(IClass iClass);

    void delete(String classId) throws BusinessServiceException;

    Class update(Class ooadClass) throws BusinessServiceException;

    void importStudents(String classId, Map<String, Object> payload) throws Exception;
}
