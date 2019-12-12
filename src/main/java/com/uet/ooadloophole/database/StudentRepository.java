package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findBy_id(String _id);
    Student findByStudentId(String studentId);
}
