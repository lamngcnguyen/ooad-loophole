package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Role;
import com.uet.ooadloophole.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findBy_id(String _id);

    List<User> findAllByFullNameIgnoreCase(String fullName);

    @Query(value = "{ roles: {$elemMatch: {$id: ?0}}}")
    List<User> findAllByRole(ObjectId roleId);
}
