package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.requirement_repositories.RequirementsRepository;
import com.uet.ooadloophole.database.system_repositories.StudentRepository;
import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementLog;
import com.uet.ooadloophole.model.business.requirement_elements.RequirementSpecFile;
import com.uet.ooadloophole.model.business.system_elements.Student;
import com.uet.ooadloophole.service.SecureUserService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementLogService;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {
    @Autowired
    private RequirementsRepository requirementsRepository;
    @Autowired
    private RequirementLogService requirementLogService;
    @Autowired
    private SecureUserService secureUserService;
    @Autowired
    private StudentRepository studentRepository;

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
    public List<Requirement> searchRequirement(String groupId, String keyword) {
        return requirementsRepository.findByNameContainsIgnoreCaseAndGroupId(keyword,groupId);
    }

    @Override
    public Requirement create(Requirement requirement) throws BusinessServiceException {
        try {
            requirement.setCreator(secureUserService.getCurrentUser());
            requirement.setDatetimeCreated(LocalDateTime.now());
            requirement.setRequirementSpecFile(new ArrayList<>());
            return requirementsRepository.save(requirement);
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
            if (dbRequirement.getType() == null && requirement.getType() != null) {
                log.add(requirementLogService.createLog(dbRequirement, "Set type to " + requirement.getType(), "Type Set"));
                dbRequirement.setType(requirement.getType());
            } else if (!dbRequirement.getType().equals(requirement.getType())) {
                log.add(requirementLogService.createLog(dbRequirement, "Change type from "
                        + dbRequirement.getType() + " to " + requirement.getType(), "Type Changed"));
                dbRequirement.setType(requirement.getType());
            }
            if (dbRequirement.getDescription() == null && requirement.getDescription() != null) {
                log.add(requirementLogService.createLog(dbRequirement, "Set description to " + requirement.getDescription(), "Desc Set"));
                dbRequirement.setDescription(requirement.getDescription());
            } else if (!dbRequirement.getDescription().equals(requirement.getDescription())) {
                log.add(requirementLogService.createLog(dbRequirement, "Change description from "
                        + dbRequirement.getDescription() + " to " + requirement.getDescription(), "Desc Changed"));
                dbRequirement.setDescription(requirement.getDescription());
            }

            dbRequirement.setParentReq(requirement.getParentReq());
            dbRequirement.setRequirementSpecFile(requirement.getRequirementSpecFile());
            requirementsRepository.save(dbRequirement);
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

    @Override
    public List<Requirement> getByGroup(String groupId) {
        return requirementsRepository.findByGroupId(groupId);
    }

    @Override
    public Requirement createByName(String name) throws BusinessServiceException {
        Student student = studentRepository.findByUserId(secureUserService.getCurrentUser().get_id());
        Requirement requirement = new Requirement();
        requirement.setName(name);
        requirement.setGroupId(student.getGroupId());
        return requirementsRepository.save(requirement);
    }
}
