package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.grading_repositories.AssignmentGradeRepository;
import com.uet.ooadloophole.model.business.grading_elements.AssignmentGrade;
import com.uet.ooadloophole.service.business_service.AssignmentGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentGradeServiceImpl implements AssignmentGradeService {
    @Autowired
    private AssignmentGradeRepository assignmentGradeRepository;

    @Override
    public List<AssignmentGrade> getAll() {
        return assignmentGradeRepository.findAll();
    }

    @Override
    public List<AssignmentGrade> getAllByAssignmentId(String assignmentId) {
        return assignmentGradeRepository.findAllByAssignmentId(assignmentId);
    }

    @Override
    public AssignmentGrade create(AssignmentGrade assignmentGrade) {
        assignmentGrade.setTimestamp(LocalDateTime.now());
        return assignmentGradeRepository.save(assignmentGrade);
    }

    @Override
    public AssignmentGrade update(AssignmentGrade assignmentGrade) {
        AssignmentGrade dbAssignmentGrade = assignmentGradeRepository.findBy_id(assignmentGrade.get_id());
        dbAssignmentGrade.setAssignmentId(assignmentGrade.getAssignmentId());
        dbAssignmentGrade.setValue(assignmentGrade.getValue());
        dbAssignmentGrade.setTimestamp(LocalDateTime.now());
        assignmentGradeRepository.save(dbAssignmentGrade);
        return dbAssignmentGrade;
    }
}
