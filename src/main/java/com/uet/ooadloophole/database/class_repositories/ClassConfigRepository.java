package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.ClassConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassConfigRepository extends MongoRepository<ClassConfig, String> {
    ClassConfig findBy_id(String _id);
    ClassConfig findByClassId(String classId);
}
