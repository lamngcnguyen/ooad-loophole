package com.uet.ooadloophole.database.requirement_repositories;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequirementLogRepository extends MongoRepository<RequirementLog,String> {
    RequirementLog findBy_id(String id);

    List<RequirementLog> findByRequirement(Requirement requirement);
}
