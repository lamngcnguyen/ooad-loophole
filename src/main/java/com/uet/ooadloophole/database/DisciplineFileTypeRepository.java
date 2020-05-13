package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.business.DisciplineFileType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DisciplineFileTypeRepository extends MongoRepository<DisciplineFileType, String> {
    DisciplineFileType findBy_id(String _id);
    DisciplineFileType findByName(String name);
    List<DisciplineFileType> findAllByDisciplineName(String disciplineName);
}
