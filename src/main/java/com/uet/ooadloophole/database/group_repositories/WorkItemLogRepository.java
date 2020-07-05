package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkItemLogRepository extends MongoRepository<WorkItemLog, String> {
    WorkItemLog findBy_id(String _id);

    List<WorkItemLog> findByTask(WorkItem task);
}
