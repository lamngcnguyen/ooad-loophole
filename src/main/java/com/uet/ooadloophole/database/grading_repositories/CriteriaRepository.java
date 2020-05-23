package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CriteriaRepository extends MongoRepository<Criteria, String> {
    Criteria findBy_id(String _id);
}
