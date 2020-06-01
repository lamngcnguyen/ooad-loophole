package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.BoardRepository;
import com.uet.ooadloophole.database.group_repositories.GroupRepository;
import com.uet.ooadloophole.database.group_repositories.WorkItemRepository;
import com.uet.ooadloophole.database.system_repositories.StudentRepository;
import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkItemServiceImpl implements WorkItemService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private WorkItemRepository workItemRepository;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<WorkItem> getByGroupAndStatus(String groupId, String status) {
        try {
            Board board = getBoardByGroup(groupId);
            return workItemRepository.findAllByBoardIdAndStatus(board.get_id(), status);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Board getBoardByGroup(String groupId) {
        return boardRepository.findByGroupId(groupId);
    }

    @Override
    public WorkItem createTask(String name) throws BusinessServiceException {
        Student creator = studentRepository.findByUserId(secureUserService.getCurrentUser().get_id());
        Group group = groupRepository.findBy_id(creator.getGroupId());
        Board board = boardRepository.findByGroupId(group.get_id());
        WorkItem workItem = new WorkItem();
        workItem.setName(name);
        workItem.setCreator(creator);
        if (board == null) {
            Board newBoard = new Board();
            newBoard.setGroupId(group.get_id());
            board = boardRepository.save(newBoard);
        }
        workItem.setAssignedDate(LocalDateTime.now());
        workItem.setStatus("New");
        workItem.setBoardId(board.get_id());
        return workItemRepository.save(workItem);
    }

    @Override
    public WorkItem getById(String id) {
        return workItemRepository.findBy_id(id);
    }

    @Override
    public WorkItem edit(String id, WorkItem workItem) {
        WorkItem dbWorkItem = getById(id);
        dbWorkItem.setName(workItem.getName());
        dbWorkItem.setAssignedMember(workItem.getAssignedMember());
        dbWorkItem.setDescription(workItem.getDescription());
        dbWorkItem.setPriority(workItem.getPriority());
        dbWorkItem.setStatus(workItem.getStatus());
        return workItemRepository.save(dbWorkItem);
    }

    @Override
    public void delete(String id) throws BusinessServiceException {
        Student currentStudent = studentRepository.findByUserId(secureUserService.getCurrentUser().get_id());
        WorkItem workItem = getById(id);
        if (workItem.getCreator().get_id().equals(currentStudent.get_id())) {
            workItemRepository.delete(getById(id));
        } else {
            throw new BusinessServiceException("Current student is not item creator");
        }
    }
}
