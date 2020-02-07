package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SemesterRepository extends MongoRepository<Semester, String> {
    Semester findBy_id(String _id);
}
