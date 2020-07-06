package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequirementLogService {
    RequirementLog createLog(Requirement requirement, String description, String type) throws BusinessServiceException;

    List<RequirementLog> getAllLog(Requirement requirement);
}
