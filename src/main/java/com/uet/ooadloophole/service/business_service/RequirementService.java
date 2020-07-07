package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequirementService {
    Requirement create(Requirement requirement) throws BusinessServiceException;

    List<Requirement> getAllRequirement();

    List<Requirement> getByGroup(String groupId);

    Requirement createByName(String name) throws BusinessServiceException;

    List<RequirementLog> update(String id, Requirement requirement) throws BusinessServiceException;

    void delete(String id) throws BusinessServiceException;

    Requirement getById(String id) throws BusinessServiceException;

    void assignRequirementIdToFile(String id, RequirementSpecFile file);

    List<Requirement> searchRequirement(String groupId, String keyword);
}
