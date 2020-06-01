package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.grading_elements.Grade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GradeService {
    List<Grade> getAll();

    List<Grade> getAllByIterationId(String iterationId);

    List<Grade> getAllByGraderId(String graderId);

    Grade getByIterationIdAndGraderId(String iterationId, String gradeId);

    Grade create (Grade grade);

    Grade update (String gradeId, Grade grade);
}
