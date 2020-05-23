package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.TopicSpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicSpecFileRepository extends MongoRepository<TopicSpecFile, String> {
    TopicSpecFile findBy_id(String _id);

    List<TopicSpecFile> findAllByTopicId(String topicId);
}
