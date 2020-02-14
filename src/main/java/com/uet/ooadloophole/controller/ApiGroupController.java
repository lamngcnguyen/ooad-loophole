package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.business.Group;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.Topic;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/groups")
public class ApiGroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Group> getGroupById(@PathVariable String id) {
        try {
            Group result = groupService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/{id}/students", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> getStudents(@PathVariable String id) {
        List<Student> students = studentService.getByGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @RequestMapping(value = "/{id}/topic", method = RequestMethod.GET)
    public ResponseEntity<Object> getTopic(@PathVariable String id) {
        try {
            Topic topic = topicService.getByGroupId(id);
            return ResponseEntity.status(HttpStatus.OK).body(topic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> createGroup(@RequestBody Group group) {
        try {
            Group newGroup = groupService.create(group);
            return ResponseEntity.status(HttpStatus.OK).body(newGroup);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateGroup(@RequestBody Group group) {
        try {
            Group updatedGroup = groupService.update(group);
            return ResponseEntity.status(HttpStatus.OK).body(updatedGroup);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteGroup(@PathVariable String id) {
        try {
            groupService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Group>> searchGroup(@RequestParam String keyword) {
        List<Group> groups = groupService.searchByName(keyword);
        HttpStatus httpStatus = (groups.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(groups);
    }
}
