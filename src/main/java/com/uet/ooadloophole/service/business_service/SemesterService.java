package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.class_elements.Semester;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SemesterService {
    List<Semester> getAll();

    Semester getById(String id) throws BusinessServiceException;

    Semester create(Semester semester);

    Semester update(String id, Semester semester) throws BusinessServiceException;

    void delete(String semesterId) throws BusinessServiceException;
}
