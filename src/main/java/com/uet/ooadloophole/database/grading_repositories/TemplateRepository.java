package com.uet.ooadloophole.database.grading_repositories;

import com.uet.ooadloophole.model.business.grading_elements.Template;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<Template, String> {
}
