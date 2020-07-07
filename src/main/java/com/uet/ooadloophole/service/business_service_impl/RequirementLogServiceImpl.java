package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.requirement_repositories.RequirementLogRepository;
import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementLogService;
import com.uet.ooadloophole.service.business_service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequirementLogServiceImpl implements RequirementLogService {
    @Autowired
    private RequirementLogRepository requirementLogRepository;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentService studentService;

    @Override
    public RequirementLog createLog(Requirement requirement, String description, String type) throws BusinessServiceException {
        Student student = studentService.getByUserId(secureUserService.getCurrentUser().get_id());
        RequirementLog log = new RequirementLog();
        log.setRequirement(requirement);
        //log.setCreator(student);
        //log.setType(type);
        log.setWhatChanged(description);
        //log.setTime(LocalDateTime.now());
        return requirementLogRepository.save(log);
    }

    @Override
    public List<RequirementLog> getAllLog(Requirement requirement) {
        return requirementLogRepository.findByRequirement(requirement);
    }
}
