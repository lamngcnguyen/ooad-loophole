package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.RepoFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepoFileRepository extends MongoRepository<RepoFile, String> {
    List<RepoFile> findAllByIterationId(String iterationId);

    List<RepoFile> findAllByGroupIdAndFileNameAndPath(String groupId, String fileName, String path);

    RepoFile findBy_id(String _id);
}
