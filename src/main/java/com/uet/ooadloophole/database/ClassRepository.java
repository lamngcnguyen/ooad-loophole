package com.uet.ooadloophole.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.uet.ooadloophole.model.Class;

import java.util.List;

public interface ClassRepository extends MongoRepository<com.uet.ooadloophole.model.Class, String> {
    Class findBy_id(String _id);
    List<Class> findAllByTeacherId(String teacherId);
    Class findClassByClassName(String className);
}
