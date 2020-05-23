package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.interfaces.IAssignment;
import com.uet.ooadloophole.model.business.class_elements.Assignment;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/assignments")
public class ApiAssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private ConverterService converterService;

    Gson gson = new Gson();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Assignment> getById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(assignmentService.getById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Assignment> create(@RequestBody IAssignment iAssignment) {
        Assignment assignment = assignmentService.create(converterService.convertAssignmentInterface(iAssignment));
        return ResponseEntity.status(HttpStatus.OK).body(assignment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Assignment> update(@PathVariable String id, @RequestBody IAssignment iAssignment) {
        Assignment assignment = assignmentService.edit(id, converterService.convertAssignmentInterface(iAssignment));
        return ResponseEntity.status(HttpStatus.OK).body(assignment);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        assignmentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("deleted")));
    }
}
