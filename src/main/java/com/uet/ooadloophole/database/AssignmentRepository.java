package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    Assignment findBy_id(String _id);

    List<Assignment> findByClassId(String classId);
}
