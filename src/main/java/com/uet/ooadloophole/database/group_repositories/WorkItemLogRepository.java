package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkItemLogRepository extends MongoRepository<WorkItemLog, String> {
    WorkItemLog findBy_id(String _id);
}
