package com.uet.ooadloophole.database.class_repositories;

import com.uet.ooadloophole.model.business.class_elements.Class;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClassRepository extends MongoRepository<Class, String> {
    Class findBy_id(String _id);

    Class getByTeacherIdAndClassName(String teacherId, String className);

    List<Class> findAllByTeacherIdAndClassNameLikeIgnoreCase(String teacherId, String className);

    List<Class> findAllByTeacherId(String teacherId);

    List<Class> findAllByClassNameLikeIgnoreCase(String className);

    List<Class> findAllByTeacherIdAndSemesterId(String teacherId, String semesterId);
}
