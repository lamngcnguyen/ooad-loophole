package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.TopicSpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpecFileRepository extends MongoRepository<TopicSpecFile, String> {
    TopicSpecFile findBy_id(String _id);

    List<TopicSpecFile> findAllByTopicId(String topicId);
}
