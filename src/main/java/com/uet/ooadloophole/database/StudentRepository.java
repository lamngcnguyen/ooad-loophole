package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findBy_id(String _id);

    Student findByStudentId(String studentId);

    Student findByUserId(String userId);

    Student findBy_idNot(String _id);

    Student findBy_idNotAndStudentId(String _id, String studentId);

    List<Student> findAllByClassId(String classId);

    List<Student> findAllByGroupId(String groupId);

    List<Student> deleteAllByClassId(String classId);

    List<Student> findAllByFullNameLikeIgnoreCaseOrStudentIdLike(String fullName, String studentId);

    int countAllByClassId(String classId);
}
