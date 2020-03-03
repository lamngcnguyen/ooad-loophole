package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Template;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<Template, String> {
}
