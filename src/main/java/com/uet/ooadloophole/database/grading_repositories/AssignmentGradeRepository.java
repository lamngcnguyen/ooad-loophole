package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.AssignmentGrade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssignmentGradeRepository extends MongoRepository<AssignmentGrade, String>{
    AssignmentGrade findBy_id(String _id);

    List<AssignmentGrade> findAllByAssignmentId(String assignmentId);

    AssignmentGrade findByGroupIdAndAssignmentId(String groupId, String assignmentId);
}
