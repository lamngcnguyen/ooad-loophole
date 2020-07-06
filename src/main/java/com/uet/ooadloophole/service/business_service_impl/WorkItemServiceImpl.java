package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.WorkItemRepository;
import com.uet.ooadloophole.database.system_repositories.StudentRepository;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.IterationService;
import com.uet.ooadloophole.service.business_service.WorkItemLogService;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkItemServiceImpl implements WorkItemService {
    @Autowired
    private WorkItemRepository workItemRepository;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WorkItemLogService workItemLogService;
    @Autowired
    private IterationService iterationService;

    @Override
    public List<WorkItem> getByGroupAndStatus(String groupId, String status) {
        try {
            return workItemRepository.findAllByGroupIdAndStatus(groupId, status);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public WorkItem createTask(String name) throws BusinessServiceException {
        Student creator = studentRepository.findByUserId(secureUserService.getCurrentUser().get_id());
        WorkItem workItem = new WorkItem();
        workItem.setName(name);
        workItem.setCreator(creator);
        workItem.setIteration(iterationService.getCurrentIteration(creator.getGroupId()));
        workItem.setCreatedDate(LocalDateTime.now());
        workItem.setStatus("New");
        workItem.setPriority(1);
        workItem.setGroupId(creator.getGroupId());
        WorkItem dbWorkItem = workItemRepository.save(workItem);
        workItemLogService.createLog(dbWorkItem, "Work item \"" + dbWorkItem.getName() + "\" created", "Created");
        return dbWorkItem;
    }

    @Override
    public WorkItem getById(String id) {
        return workItemRepository.findBy_id(id);
    }

    @Override
    public WorkItem assignMember(String studentId, String id) {
        WorkItem dbWorkItem = getById(id);
        dbWorkItem.setAssignedMember(studentRepository.findBy_id(studentId));
        return workItemRepository.save(dbWorkItem);
    }

    @Override
    public List<WorkItemLog> edit(String id, WorkItem workItem) throws BusinessServiceException {
        WorkItem dbWorkItem = getById(id);
        List<WorkItemLog> workItemLogs = new ArrayList<>();
        if (!dbWorkItem.getName().equals(workItem.getName())) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Name changed from \"" + dbWorkItem.getName() + "\" to \"" + workItem.getName() + "\"", "Name edited"));
            dbWorkItem.setName(workItem.getName());
        }
        if (dbWorkItem.getAssignedMember() == null && workItem.getAssignedMember() != null) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Work item assigned to " + workItem.getAssignedMember().getFullName(), "Assigned to member"));
            dbWorkItem.setAssignedMember(workItem.getAssignedMember());
        } else if (dbWorkItem.getAssignedMember() != null && !dbWorkItem.getAssignedMember().get_id().equals(workItem.getAssignedMember().get_id())) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Assignee changed from " + dbWorkItem.getAssignedMember().getFullName() + " to " + workItem.getAssignedMember().getFullName(), "Assignee changed"));
            dbWorkItem.setAssignedMember(workItem.getAssignedMember());
        }
        if (dbWorkItem.getIteration() == null) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Work item iteration set to " + workItem.getIteration().getName(), "Iteration set"));
            dbWorkItem.setIteration(workItem.getIteration());
        } else if (!dbWorkItem.getIteration().get_id().equals(workItem.getIteration().get_id())) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Iteration changed from " + dbWorkItem.getIteration().getName() + " to " + workItem.getIteration().getName(), "Iteration changed"));
            dbWorkItem.setIteration(workItem.getIteration());
        }
        if (dbWorkItem.getDescription() == null && workItem.getDescription() != null) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Added description \"" + workItem.getDescription() + "\"", "Description added"));
            dbWorkItem.setDescription(workItem.getDescription());
        } else if (!dbWorkItem.getDescription().equals(workItem.getDescription())) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Changed from \"" + dbWorkItem.getDescription() + "\" to \"" + workItem.getDescription() + "\"", "Description changed"));
            dbWorkItem.setDescription(workItem.getDescription());
        }
        if (dbWorkItem.getPriority() != workItem.getPriority()) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Priority changed from " + dbWorkItem.getPriority() + " to " + workItem.getPriority(), "Priority changed"));
            dbWorkItem.setPriority(workItem.getPriority());
        }
        if (!dbWorkItem.getStatus().equals(workItem.getStatus()) && workItem.getStatus() != null) {
            workItemLogs.add(workItemLogService.createLog(dbWorkItem, "Status changed from " + dbWorkItem.getStatus() + " to " + workItem.getStatus(), "Status changed"));
            dbWorkItem.setStatus(workItem.getStatus());
        }
        workItemRepository.save(dbWorkItem);
        return workItemLogs;
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
