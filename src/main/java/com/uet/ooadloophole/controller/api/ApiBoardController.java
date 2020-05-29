package com.uet.ooadloophole.controller.api;

import com.uet.ooadloophole.model.business.group_elements.WorkItem;
import com.uet.ooadloophole.service.business_service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/board")
public class ApiBoardController {
    @Autowired
    private WorkItemService workItemService;

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public ResponseEntity<WorkItem> getItem(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(workItemService.getById(id));
    }
}
