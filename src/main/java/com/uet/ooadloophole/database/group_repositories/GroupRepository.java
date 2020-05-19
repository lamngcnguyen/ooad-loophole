package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findBy_id(String _id);

    Group findByGroupName(String name);

    Group findByClassIdAndGroupName(String classId, String groupName);

    Group findBy_idNotAndGroupName(String _id, String groupName);

    List<Group> findAllByClassId(String classId);

    List<Group> findAllByGroupNameLikeIgnoreCase(String groupName);

    void deleteAllByClassId(String classId);
}
