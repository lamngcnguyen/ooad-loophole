package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.ClassConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassConfigRepository extends MongoRepository<ClassConfig, String> {
}
