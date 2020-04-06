package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository extends MongoRepository<Invitation, String> {
    Invitation findBy_id(String _id);
}
