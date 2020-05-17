package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CriteriaRepository extends MongoRepository<Criteria, String> {
    Criteria findBy_id(String _id);
}
