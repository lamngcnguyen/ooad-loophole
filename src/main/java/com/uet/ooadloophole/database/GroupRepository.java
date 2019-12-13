package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(String name);
    List<Group> findAllByClassId(String classId);
    Group findBy_id(String _id);
}
