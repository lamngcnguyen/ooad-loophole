package com.uet.ooadloophole.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.uet.ooadloophole.model.Class;

public interface ClassRepository extends MongoRepository<com.uet.ooadloophole.model.Class, String> {

}
