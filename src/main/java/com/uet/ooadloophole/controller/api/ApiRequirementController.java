package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.model.business.requirement_elements.Requirement;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/requirement")
public class ApiRequirementController {
    @Autowired
    private RequirementService requirementService;

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
    public ResponseEntity<List<Requirement>> findAllRequirement(@PathVariable String groupId) {
        return ResponseEntity.status(HttpStatus.OK).body(requirementService.getByGroup(groupId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRequirement(@PathVariable String id) {
        try {
            requirementService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("deleted");
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createRequirement(@RequestBody Requirement requirement) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(requirementService.create(requirement));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createRequirementWithName(@RequestParam String name) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(requirementService.createByName(name));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRequirement(@RequestParam String id, @RequestBody Requirement requirement) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(requirementService.update(id, requirement));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Requirement> getRequirement(@PathVariable String id) throws BusinessServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(requirementService.getById(id));
    }
}
