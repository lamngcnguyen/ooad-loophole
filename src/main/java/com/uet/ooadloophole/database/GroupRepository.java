package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findBy_id(String _id);

    Group findByGroupName(String name);

    Group findByClassIdAndGroupName(String classId, String groupName);

    Group findByTopicId(String topicId);

    Group findBy_idNotAndGroupName(String _id, String groupName);

    List<Group> findAllByClassId(String classId);

    List<Group> findAllByClassIdAndTopicIdIsNull(String classId);

    List<Group> findAllByGroupNameLikeIgnoreCase(String groupName);

    void deleteAllByClassId(String classId);
}
