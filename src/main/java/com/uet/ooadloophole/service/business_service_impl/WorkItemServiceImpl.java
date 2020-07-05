package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.WorkItemLogRepository;
import com.uet.ooadloophole.database.group_repositories.WorkItemRepository;
import com.uet.ooadloophole.database.system_repositories.StudentRepository;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.IterationService;
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
    private WorkItemLogRepository workItemLogRepository;
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
        return workItemRepository.save(workItem);
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
    public WorkItem edit(String id, WorkItem workItem) {
        WorkItem dbWorkItem = getById(id);
        dbWorkItem.setName(workItem.getName());
        dbWorkItem.setAssignedMember(workItem.getAssignedMember());
        dbWorkItem.setIteration(workItem.getIteration());
        dbWorkItem.setDescription(workItem.getDescription());
        dbWorkItem.setPriority(workItem.getPriority());
        dbWorkItem.setStatus(workItem.getStatus());

        WorkItemLog workItemLog = new WorkItemLog();
        workItemLog.setTask(dbWorkItem);
        workItemLog.setDescription(workItem.getName() + " edited");
        workItemLog.setTimeStamp(LocalDateTime.now());
//        workItemLog.setStudentId("");
//        workItemLogRepository.save(workItemLog);
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
