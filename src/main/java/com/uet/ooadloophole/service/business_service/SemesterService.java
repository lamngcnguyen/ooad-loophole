package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.Semester;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

@Service
public interface SemesterService {
    Semester getById(String id) throws BusinessServiceException;

    Semester create(Semester semester);

    Semester update(Semester semester) throws BusinessServiceException;

    void delete(String semesterId) throws BusinessServiceException;
}
