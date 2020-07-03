package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRepository extends MongoRepository<Topic, String> {
    Topic findBy_id(String _id);

    Topic findByGroupId(String groupId);

    List<Topic> findAllByClassId(String classId);

    List<Topic> findAllByClassIdAndGroupIdNull(String classId);

    List<Topic> deleteAllByClassId(String classId);

    List<Topic> findAllByNameLikeOrDescriptionsLike(String name, String descriptions);
}
