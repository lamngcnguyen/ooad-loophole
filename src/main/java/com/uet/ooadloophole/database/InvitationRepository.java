package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository extends MongoRepository<Request, String> {
    Request findBy_id(String _id);
}
