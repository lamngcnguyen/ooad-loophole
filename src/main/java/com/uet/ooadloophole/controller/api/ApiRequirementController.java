package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/requirement")
public class ApiRequirementController {
    @Autowired
    private RequirementService requirementService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Requirement> findAllRequirement() {
        return requirementService.getAllRequirement();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteRequirement(@PathVariable String id) throws BusinessServiceException {
        requirementService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Requirement createRequirement(@RequestBody Requirement requirement) throws BusinessServiceException {
        requirementService.create(requirement);
        return requirement;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Requirement updateRequirement(@RequestParam String id, @RequestBody Requirement requirement) throws BusinessServiceException {
        requirementService.update(id, requirement);
        return requirementService.getById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Requirement getRequirement(@PathVariable String id) throws BusinessServiceException {
        return requirementService.getById(id);
    }

    @RequestMapping(value = "/{id}/child", method = RequestMethod.GET)
    public List<Requirement> getChildRequirement(@PathVariable String id) throws BusinessServiceException {
        return requirementService.getById(id).getChildRequirements();
    }
}
