package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkItemRepository extends MongoRepository<WorkItem, String> {
    WorkItem findBy_id(String _id);

    List<WorkItem> findAllByGroupIdAndStatus(String groupId, String status);
}
