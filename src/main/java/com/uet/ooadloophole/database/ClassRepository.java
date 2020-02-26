package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Class;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClassRepository extends MongoRepository<Class, String> {
    Class findBy_id(String _id);

    List<Class> findAllByTeacherId(String teacherId);

    List<Class> findAllByClassNameLikeIgnoreCase(String className);

    List<Class> findAllByTeacherIdAndSemesterId(String teacherId, String semesterId);
}
