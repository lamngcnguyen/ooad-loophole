package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkItemFileRepository extends MongoRepository<WorkItemFile, String> {
    WorkItemFile findBy_id(String _id);
}
