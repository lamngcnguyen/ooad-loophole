package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkItemService {

    List<Board> getBoardByGroup(String groupId);

    Board createBoard(Board board);

    WorkItem createTask(WorkItem workItem);

    WorkItem getById(String id);

}
