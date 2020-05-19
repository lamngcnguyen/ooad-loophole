package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SemesterRepository extends MongoRepository<Semester, String> {
    Semester findBy_id(String _id);
}
