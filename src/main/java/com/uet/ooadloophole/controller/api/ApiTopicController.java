package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.ResponseMessage;
import com.uet.ooadloophole.controller.interface_model.dto.DTOTopic;
import com.uet.ooadloophole.model.business.group_elements.Group;
import com.uet.ooadloophole.model.business.class_elements.Topic;
import com.uet.ooadloophole.model.business.group_elements.Request;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/topics")
public class ApiTopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private ConverterService converterService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getTopicById(@PathVariable String id) {
        try {
            Topic topic = topicService.getById(id);
            DTOTopic dtoTopic = converterService.convertToDTOTopic(topic);
            return ResponseEntity.status(HttpStatus.OK).body(dtoTopic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/group", method = RequestMethod.GET)
    public ResponseEntity<Object> getGroup(@PathVariable String id) {
        try {
            Topic topic = topicService.getById(id);
            Group group = groupService.getById(topic.getGroupId());
            return ResponseEntity.status(HttpStatus.OK).body(group);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/assign", method = RequestMethod.POST)
    public ResponseEntity<Object> assignToGroup(@PathVariable String id, String groupId) {
        try {
            Topic topic = topicService.assignToGroup(id, groupId);
            return ResponseEntity.status(HttpStatus.OK).body(topic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //TODO: createInvitation Spring Security Rules for these requests
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        Topic newTopic = topicService.create(topic);
        return ResponseEntity.status(HttpStatus.OK).body(newTopic);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateTopic(@RequestBody Topic topic, @PathVariable String id) {
        try {
            Topic updatedTopic = topicService.update(id, topic);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTopic);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTopic(@PathVariable String id) {
        try {
            topicService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Topic>> searchTopic(@RequestParam String keyword) {
        List<Topic> result = topicService.searchByNameOrDescription(keyword);
        HttpStatus httpStatus = (result.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(result);
    }
}
