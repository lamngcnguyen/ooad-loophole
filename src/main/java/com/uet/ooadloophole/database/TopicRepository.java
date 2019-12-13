package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRepository extends MongoRepository<Topic, String> {
    Topic findBy_id(String _id);

    List<Topic> findAllByClassId(String classId);
}
