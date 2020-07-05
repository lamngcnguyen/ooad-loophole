package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.controller.interface_model.interfaces.IWorkItem;
import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.WorkItemLogService;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/board")
public class ApiBoardController {
    @Autowired
    private WorkItemService workItemService;
    @Autowired
    private WorkItemLogService workItemLogService;
    @Autowired
    private ConverterService converterService;

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
    public ResponseEntity<Object> editItem(@PathVariable String id, @RequestBody IWorkItem iWorkItem) {
        try {
            WorkItem workItem = converterService.convertWorkItemInterface(iWorkItem);
            return ResponseEntity.status(HttpStatus.OK).body(workItemService.edit(id, workItem));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        try {
            workItemService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("deleted");
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/item/{id}/logs", method = RequestMethod.GET)
    public ResponseEntity<Object> getLogs(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(workItemLogService.getByTask(workItemService.getById(id)));
    }

    @RequestMapping(value = "/priorities", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getPriorities() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList("Low", "Medium", "High"));
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.asList("New", "Approved", "Committed", "Done"));
    }
}
