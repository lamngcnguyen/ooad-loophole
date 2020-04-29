package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.ClassPhaseConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClassPhaseConfigRepository extends MongoRepository<ClassPhaseConfig, String> {
    List<ClassPhaseConfig> findAllByClassId(String classId);
    ClassPhaseConfig findByClassIdAndPhaseId(String classId, String phaseId);
}
