package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Iteration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IterationRepository extends MongoRepository<Iteration, String> {
    List<Iteration> findAllByClassId(String classId);
    Iteration findBy_id(String id);
    List<Iteration> deleteAllByClassId(String classId);
}
