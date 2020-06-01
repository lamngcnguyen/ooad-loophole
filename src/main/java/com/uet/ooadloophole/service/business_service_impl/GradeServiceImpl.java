package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.grading_repositories.GradeRepository;
import com.uet.ooadloophole.model.business.grading_elements.Grade;
import com.uet.ooadloophole.service.business_service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public List<Grade> getAll() {
        return gradeRepository.findAll();
    }

    @Override
    public List<Grade> getAllByIterationId(String iterationId) {
        return gradeRepository.findAllByIterationId(iterationId);
    }

    @Override
    public List<Grade> getAllByGraderId(String graderId) {
        return gradeRepository.findAllByGraderId(graderId);
    }

    @Override
    public Grade getByIterationIdAndGraderId(String iterationId, String gradeId) {
        return gradeRepository.findByIterationIdAndGraderId(iterationId, gradeId);
    }

    @Override
    public Grade create(Grade grade) {
        grade.setTimestamp(LocalDateTime.now());
        return gradeRepository.save(grade);
    }

    @Override
    public Grade update(String gradeId, Grade grade) {
        Grade dbGrade = gradeRepository.findBy_id(gradeId);
        dbGrade.setGraderId(grade.getGraderId());
        dbGrade.setIterationId(grade.getIterationId());
        dbGrade.setValue(grade.getValue());
        dbGrade.setTimestamp(LocalDateTime.now());
        gradeRepository.save(dbGrade);
        return dbGrade;
    }
}
