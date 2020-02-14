package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.RepoFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoFileRepository extends MongoRepository<RepoFile, String> {
}
