package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByStudentId(String studentId);
}
