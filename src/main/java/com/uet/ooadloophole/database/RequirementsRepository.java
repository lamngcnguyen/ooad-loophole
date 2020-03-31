package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Requirement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequirementsRepository extends MongoRepository<Requirement, String> {
    Requirement findBy_id(String id);

    List<Requirement> findAllByParentIdNull();

    List<Requirement> findByParentId(String id);

}
