package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.ClassPhaseConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassPhaseConfigRepository extends MongoRepository<ClassPhaseConfig, String> {
}
