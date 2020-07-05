package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.WorkItemLogRepository;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItemLog;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.WorkItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkItemLogServiceImpl implements WorkItemLogService {
    @Autowired
    private WorkItemLogRepository workItemLogRepository;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentService studentService;

    @Override
    public WorkItemLog createLog(WorkItem workItem, String description, String type) throws BusinessServiceException {
        Student student = studentService.getByUserId(secureUserService.getCurrentUser().get_id());
        WorkItemLog workItemLog = new WorkItemLog();
        workItemLog.setTask(workItem);
        workItemLog.setStudent(student);
        workItemLog.setType(type);
        workItemLog.setDescription(description);
        workItemLog.setTimeStamp(LocalDateTime.now());
        return workItemLogRepository.save(workItemLog);
    }

    @Override
    public List<WorkItemLog> getByTask(WorkItem workItem) {
        return workItemLogRepository.findByTask(workItem);
    }
}
