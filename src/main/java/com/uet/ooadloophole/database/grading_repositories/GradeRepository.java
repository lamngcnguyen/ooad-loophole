package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GradeRepository extends MongoRepository<Grade, String>{
    Grade findBy_id(String _id);

    List<Grade> findAllByIterationId(String iterationId);

    List<Grade> findAllByGraderId(String graderId);

    Grade findByIterationIdAndGraderId(String iterationId, String graderId);
}
