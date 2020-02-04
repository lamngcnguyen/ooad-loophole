package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(String name);
    Group findByClassIdAndGroupName(String classId, String groupName);
    Group findByTopicId(String topicId);
    List<Group> findAllByClassId(String classId);
    List<Group> findAllByClassIdAndTopicIdIsNull(String classId);
    Group findBy_id(String _id);
    List<Group> deleteAllByClassId(String classId);
}