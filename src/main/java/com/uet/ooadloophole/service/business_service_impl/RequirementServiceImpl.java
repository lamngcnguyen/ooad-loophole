package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.requirement_repositories.RequirementsRepository;
import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {
    @Autowired
    private RequirementsRepository requirementsRepository;


    @Override
    public Requirement getById(String id) throws BusinessServiceException {
        Requirement requirement = requirementsRepository.findBy_id(id);
        if (requirement == null) {
            throw new BusinessServiceException("Can't find requirement");
        }
        return requirement;
    }

    @Override
    public void assignRequirementIdToFile(String id, RequirementSpecFile file) {
        file.setRequirementId(id);
    }

    @Override
    public void create(Requirement requirement) throws BusinessServiceException {
        try {
            requirementsRepository.save(requirement);
        } catch (Exception e) {
            throw new BusinessServiceException("Error creating requirement: " + e.getMessage());
        }
    }

    @Override
    public Requirement update(String id, Requirement requirement) throws BusinessServiceException {
        try {
            Requirement dbRequirement = getById(id);
            dbRequirement.setName(requirement.getName());
            dbRequirement.setDescription(requirement.getDescription());
            dbRequirement.setChildRequirements(requirement.getChildRequirements());
            dbRequirement.setRequirementSpecFile(requirement.getRequirementSpecFile());
            return requirementsRepository.save(dbRequirement);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Error updating requirement: " + e.getMessage());
        }

    }

    @Override
    public void delete(String id) throws BusinessServiceException {
        try {
            Requirement requirement = getById(id);
            requirementsRepository.delete(requirement);
        } catch (BusinessServiceException e) {
            throw new BusinessServiceException("Unable to delete requirement: " + e.getMessage());
        }
    }


    @Override
    public List<Requirement> getAllRequirement() {
        return requirementsRepository.findAll();
    }

    @Override
    public List<Requirement> getAllChildRequirement(String id) throws BusinessServiceException {
        Requirement requirement = getById(id);
        return requirement.getChildRequirements();
    }


    @Override
    public Requirement getParentRequirement(String id) throws BusinessServiceException {
        Requirement child = getById(id);
        String parentId = child.getParentId();
        if (parentId == null) {
            return null;
        }
        return getById(parentId);
    }
}
