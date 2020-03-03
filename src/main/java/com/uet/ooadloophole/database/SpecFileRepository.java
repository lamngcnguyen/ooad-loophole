package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.SpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpecFileRepository extends MongoRepository<SpecFile, String> {
    SpecFile findBy_id(String _id);

    List<SpecFile> findAllByTopicId(String topicId);
}
