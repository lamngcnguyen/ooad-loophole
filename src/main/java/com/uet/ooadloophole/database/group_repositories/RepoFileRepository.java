package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.RepoFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RepoFileRepository extends MongoRepository<RepoFile, String> {
    List<RepoFile> findAllByIterationId(String iterationId);

    List<RepoFile> findAllByIterationIdAndDeletedAndType(String iterationId, boolean deleted, String type);

    List<RepoFile> findAllByGroupIdAndFileNameAndPath(String groupId, String fileName, String path);

    RepoFile findBy_id(String _id);

    List<RepoFile> findByGroupIdAndTimeStampBeforeAndType(String groupId, LocalDateTime timeStamp, String type);
}
