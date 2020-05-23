package com.uet.ooadloophole.database.system_repositories;

import com.uet.ooadloophole.model.business.system_elements.LoopholeUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<LoopholeUser, String> {
    LoopholeUser findByUsername(String username);

    LoopholeUser findByEmail(String email);

    LoopholeUser findBy_id(String _id);

    List<LoopholeUser> findAllByFullNameIgnoreCase(String fullName);

    @Query(value = "{ roles: {$elemMatch: {$id: ?0}}}")
    List<LoopholeUser> findAllByRole(ObjectId roleId);
}
