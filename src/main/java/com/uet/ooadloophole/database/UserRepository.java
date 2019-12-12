package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    User findBy_id(String _id);
}
