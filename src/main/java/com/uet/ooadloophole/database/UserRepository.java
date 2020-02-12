package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findBy_id(String _id);

    List<User> findAllByFullNameIgnoreCase(String fullName);
}
