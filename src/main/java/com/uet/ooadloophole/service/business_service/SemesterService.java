package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Semester;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Controller;

@Controller
public interface SemesterService {
    Semester getById(String id) throws BusinessServiceException;

    Semester create(Semester semester);

    Semester update(Semester semester) throws BusinessServiceException;

    void delete(String semesterId) throws BusinessServiceException;
}
