package com.uet.ooadloophole.controller;

import com.uet.ooadloophole.model.Group;
import com.uet.ooadloophole.model.Topic;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/topics")
public class ApiTopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getTopicById(@PathVariable String id) {
        try {
            Topic topic = topicService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(topic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/group", method = RequestMethod.GET)
    public ResponseEntity getGroup(@PathVariable String id) {
        try {
            Topic topic = topicService.getById(id);
            Group group = groupService.getById(topic.getGroupId());
            return ResponseEntity.status(HttpStatus.OK).body(group);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //TODO: create Spring Security Rules for these requests
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity createTopic(@RequestBody Topic topic) {
        Topic newTopic = topicService.create(topic);
        return ResponseEntity.status(HttpStatus.OK).body(newTopic);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity updateTopic(@RequestBody Topic topic) {
        try {
            Topic updatedTopic = topicService.update(topic);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTopic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTopic(@PathVariable String id) {
        try {
            topicService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity searchTopic(@RequestParam String keyword) {
        List<Topic> result = topicService.searchByNameOrDescription(keyword);
        HttpStatus httpStatus = (result.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(result);
    }
}
