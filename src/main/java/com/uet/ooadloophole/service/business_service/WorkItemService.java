package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkItemService {
    List<WorkItem> getByGroupAndStatus(String groupId, String status);

    WorkItem createTask(String name) throws BusinessServiceException;

    WorkItem getById(String id);

    WorkItem assignMember(String studentId, String id);

    WorkItem edit(String id, WorkItem workItem);

    void delete(String id) throws BusinessServiceException;
}
