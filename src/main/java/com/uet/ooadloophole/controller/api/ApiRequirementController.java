package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.model.business.Requirement;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/api/requirement")
public class ApiRequirementController {
    @Autowired
    private RequirementService requirementService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Requirement> findAllRequirement() {
        return requirementService.getAllRequirement();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteRequirement(@RequestParam String id) throws BusinessServiceException {
        requirementService.delete(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Requirement createRequirement(@RequestBody Requirement requirement) throws BusinessServiceException {
        requirementService.create(requirement);
        return requirement;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Requirement updateRequirement(@RequestParam String id, @RequestBody Requirement requirement) throws BusinessServiceException {
        requirementService.update(id, requirement);
        return requirementService.getById(id);
    }

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public Requirement getRequirement(@RequestParam String id) throws BusinessServiceException {
        return requirementService.getById(id);
    }

    @RequestMapping(value = "/id/child", method = RequestMethod.GET)
    public List<Requirement> getChildRequirement(@RequestParam String id) throws BusinessServiceException {
        return requirementService.getById(id).getChildRequirements();
    }
}
