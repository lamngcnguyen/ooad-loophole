package com.uet.ooadloophole.database.requirement_repositories;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequirementsRepository extends MongoRepository<Requirement, String> {
    Requirement findBy_id(String id);

    List<Requirement> findByNameContainsIgnoreCaseAndGroupId(String name, String groupId);

    List<Requirement> findByGroupId(String groupId);
}
