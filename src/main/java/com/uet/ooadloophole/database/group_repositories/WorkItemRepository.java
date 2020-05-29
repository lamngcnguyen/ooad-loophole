package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkItemRepository extends MongoRepository<WorkItem, String> {
    WorkItem findBy_id(String _id);
}
