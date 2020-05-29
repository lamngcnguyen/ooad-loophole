package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/board")
public class ApiBoardController {
    @Autowired
    private WorkItemService workItemService;

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public ResponseEntity<WorkItem> getItem(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(workItemService.getById(id));
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ResponseEntity<Object> createItemByName(String name) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(workItemService.createTask(name));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.PUT)
    public ResponseEntity<WorkItem> editItem(@PathVariable String id, @RequestBody WorkItem workItem) {
        return ResponseEntity.status(HttpStatus.OK).body(workItemService.edit(id, workItem));
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> editItem(@PathVariable String id) {
        try {
            workItemService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("deleted");
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
