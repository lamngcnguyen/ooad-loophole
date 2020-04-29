package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Phase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhaseRepository extends MongoRepository<Phase, String> {
    Phase findByName(String name);
}
