package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
}
