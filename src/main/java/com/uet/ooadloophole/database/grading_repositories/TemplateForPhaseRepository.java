package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.TemplateForPhase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateForPhaseRepository extends MongoRepository<TemplateForPhase, String> {
}
