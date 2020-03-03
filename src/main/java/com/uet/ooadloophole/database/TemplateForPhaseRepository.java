package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.TemplateForPhase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateForPhaseRepository extends MongoRepository<TemplateForPhase, String> {
}
