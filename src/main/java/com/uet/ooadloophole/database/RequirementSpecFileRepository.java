package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.RequirementSpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequirementSpecFileRepository extends MongoRepository<RequirementSpecFile, String> {
    RequirementSpecFile findBy_id(String id);
}
