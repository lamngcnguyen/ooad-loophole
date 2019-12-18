package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.UserFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserFileRepository extends MongoRepository<UserFile, String> {

}
