package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.class_repositories.SemesterRepository;
import com.uet.ooadloophole.model.business.class_elements.Semester;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public List<Semester> getAll() {
        return semesterRepository.findAll();
    }

    @Override
    public Semester getById(String id) throws BusinessServiceException {
        Semester result = semesterRepository.findBy_id(id);
        if (result == null) {
            throw new BusinessServiceException("No semester found for this id");
        }
        return result;
    }

    @Override
    public Semester create(Semester semester) {
        semesterRepository.save(semester);
        return semester;
    }

    @Override
    public Semester update(String id, Semester semester) throws BusinessServiceException {
        Semester dbSemester = getById(id);
        dbSemester.setName(semester.getName());
        dbSemester.setStartDate(semester.getStartDate());
        dbSemester.setEndDate(semester.getEndDate());
        semesterRepository.save(dbSemester);
        return dbSemester;
    }

    @Override
    public void delete(String semesterId) throws BusinessServiceException {
        Semester dbSemester = getById(semesterId);
        semesterRepository.delete(dbSemester);
    }
}
