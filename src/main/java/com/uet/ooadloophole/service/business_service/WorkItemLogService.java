package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkItemLogService {
    WorkItemLog createLog(WorkItem workItem, String description, String type) throws BusinessServiceException;

    List<WorkItemLog> getByTask(WorkItem workItem);
}
