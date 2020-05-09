package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Discipline;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhaseRepository extends MongoRepository<Discipline, String> {
    Discipline findByName(String name);
}
