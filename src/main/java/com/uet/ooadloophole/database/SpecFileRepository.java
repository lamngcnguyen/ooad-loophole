package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.SpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecFileRepository extends MongoRepository<SpecFile, String> {
}
