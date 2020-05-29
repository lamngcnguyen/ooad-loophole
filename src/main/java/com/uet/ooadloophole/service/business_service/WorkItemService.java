package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

@Service
public interface WorkItemService {

    Board getBoardByGroup(String groupId);

    Board createBoard(Board board);

    WorkItem createTask(String name) throws BusinessServiceException;

    WorkItem getById(String id);

    WorkItem edit(String id, WorkItem workItem);

    void delete(String id) throws BusinessServiceException;
}
