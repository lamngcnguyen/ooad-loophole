package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findBy_id(String _id);
    Student findByStudentId(String studentId);
    List<Student> findAllByClassId(String classId);
    List<Student> findAllByGroupId(String groupId);
}
