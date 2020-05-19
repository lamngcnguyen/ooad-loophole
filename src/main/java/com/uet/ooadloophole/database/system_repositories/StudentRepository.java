package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.business.system_elements.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findBy_id(String _id);

    Student findByStudentId(String studentId);

    Student findByUserId(String userId);

    Student findBy_idNot(String _id);

    Student findBy_idNotAndStudentId(String _id, String studentId);

    List<Student> getBy_idNotAndGroupId(String _id, String groupId);

    List<Student> findAllByClassId(String classId);

    List<Student> findAllByGroupId(String groupId);

    List<Student> findAllByClassIdAndGroupId(String classId, String groupId);

    List<Student> deleteAllByClassId(String classId);

    List<Student> findAllByFullNameLikeIgnoreCaseOrStudentIdLike(String fullName, String studentId);

    int countAllByClassId(String classId);
}
