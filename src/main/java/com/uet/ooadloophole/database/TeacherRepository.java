package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Teacher findByUserId(String userId);
}
