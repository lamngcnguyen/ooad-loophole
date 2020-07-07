package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.requirement_repositories.RequirementsRepository;
import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementLogService;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {
    @Autowired
    private RequirementsRepository requirementsRepository;
    @Autowired
    private RequirementLogService requirementLogService;

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
//            if (requirement.getParentReq() == null) {
//                requirement.setLevel(0);
//            } else requirement.setLevel(requirement.getParentReq().getLevel() + 1);
            requirementsRepository.save(requirement);
        } catch (Exception e) {
            throw new BusinessServiceException("Error creating requirement: " + e.getMessage());
        }
    }

    @Override
    public List<RequirementLog> update(String id, Requirement requirement) throws BusinessServiceException {
        List<RequirementLog> log = new ArrayList<>();
        try {
            Requirement dbRequirement = getById(id);
            if (!dbRequirement.getName().equals(requirement.getName())) {
                log.add(requirementLogService.createLog(dbRequirement, "Change name from "
                        + dbRequirement.getName() + " to " + requirement.getName(), "Name Changed"));
                dbRequirement.setName(requirement.getName());
            }
            if (!dbRequirement.getDescription().equals(requirement.getDescription())) {
                log.add(requirementLogService.createLog(dbRequirement, "Change description from "
                        + dbRequirement.getDescription() + " to " + requirement.getDescription(), "Desc Changed"));
                dbRequirement.setDescription(requirement.getDescription());
            }
            //dbRequirement.setChildRequirements(requirement.getChildRequirements());
//            if (dbRequirement.getLevel() != requirement.getLevel()) {
//                log.add(requirementLogService.createLog(dbRequirement, "", "Level Changed"));
//                dbRequirement.setLevel(setLevel(requirement));
//            }

            dbRequirement.setParentReq(requirement.getParentReq());
            dbRequirement.setRequirementSpecFile(requirement.getRequirementSpecFile());
            return log;
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

//    @Override
//    public List<Requirement> getAllChildRequirement(String id) throws BusinessServiceException {
//        Requirement requirement = getById(id);
//        return requirement.getChildRequirements();
//    }

    public int setLevel(Requirement requirement) throws BusinessServiceException {
        if (requirement.getParentReq() == null) return 0;
        //return requirement.getParentReq().getLevel() + 1;
        return 0;
    }

}
