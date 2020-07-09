package com.uet.ooadloophole.database.requirement_repositories;

import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequirementSpecFileRepository extends MongoRepository<RequirementSpecFile, String> {
    RequirementSpecFile findBy_id(String id);

    List<RequirementSpecFile> findByRequirementId(String requirementId);
}
