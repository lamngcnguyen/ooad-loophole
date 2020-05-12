package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.Discipline;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DisciplineRepository extends MongoRepository<Discipline, String> {
    Discipline findBy_id(String id);
    Discipline findByName(String name);
}
