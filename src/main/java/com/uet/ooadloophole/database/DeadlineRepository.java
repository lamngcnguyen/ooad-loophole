package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Deadline;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeadlineRepository extends MongoRepository<Deadline, String> {
    List<Deadline> findAllByClassId(String classId);
}
