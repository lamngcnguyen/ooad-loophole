package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.BoardRepository;
import com.uet.ooadloophole.database.group_repositories.WorkItemRepository;
import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkItemServiceImpl implements WorkItemService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private WorkItemRepository workItemRepository;

    @Override
    public List<Board> getBoardByGroup(String groupId) {
        return boardRepository.findByGroupId(groupId);
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public WorkItem createTask(WorkItem workItem) {
        return workItemRepository.save(workItem);
    }

    @Override
    public WorkItem getById(String id) {
        return workItemRepository.findBy_id(id);
    }
}
