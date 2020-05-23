package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskFileRepository extends MongoRepository<Task, String> {
}
