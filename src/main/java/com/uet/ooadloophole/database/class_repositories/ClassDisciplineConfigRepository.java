package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.ClassDisciplineConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClassDisciplineConfigRepository extends MongoRepository<ClassDisciplineConfig, String> {
    List<ClassDisciplineConfig> findAllByClassId(String classId);
    ClassDisciplineConfig findByClassIdAndDisciplineId(String classId, String disciplineId);
}
