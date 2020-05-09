package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Iteration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IterationRepository extends MongoRepository<Iteration, String> {
    List<Iteration> findAllByGroupId(String groupId);
    Iteration findBy_id(String id);
}
