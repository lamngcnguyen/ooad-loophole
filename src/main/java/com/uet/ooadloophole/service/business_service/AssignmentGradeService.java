package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.grading_elements.AssignmentGrade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssignmentGradeService {
    List<AssignmentGrade> getAll();

    List<AssignmentGrade> getAllByAssignmentId(String assignmentId);

    AssignmentGrade create(AssignmentGrade assignmentGrade);

    AssignmentGrade update(AssignmentGrade assignmentGrade);
}
