package com.uet.ooadloophole.controller.api;

import com.google.gson.Gson;
import com.uet.ooadloophole.controller.interface_model.*;
import com.uet.ooadloophole.controller.interface_model.dto.DTOGroup;
import com.uet.ooadloophole.controller.interface_model.interfaces.IGroup;
import com.uet.ooadloophole.controller.interface_model.interfaces.IInvitation;
import com.uet.ooadloophole.model.business.Group;
import com.uet.ooadloophole.model.business.Request;
import com.uet.ooadloophole.model.business.Student;
import com.uet.ooadloophole.model.business.Topic;
import com.uet.ooadloophole.service.ConverterService;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.GroupService;
import com.uet.ooadloophole.service.business_service.RequestService;
import com.uet.ooadloophole.service.business_service.StudentService;
import com.uet.ooadloophole.service.business_service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private ConverterService converterService;
    @Autowired
    private RequestService requestService;

    private Gson gson = new Gson();

    @RequestMapping(value = "/class/{classId}", method = RequestMethod.GET)
    public ResponseEntity<String> getAllGroupsByClass(@PathVariable String classId) {
        List<DTOGroup> dtoGroups = new ArrayList<>();
        for (Group group : groupService.getAllByClassId(classId)) {
            dtoGroups.add(converterService.convertToDTOGroup(group));
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(dtoGroups)));
    }

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

    @RequestMapping(value = "/{userId}/members", method = RequestMethod.GET)
    public ResponseEntity<Object> getGroupByUserId(@PathVariable String userId) {
        try {
            Student student = studentService.getByUserId(userId);
            List<Student> students = studentService.getByGroup(student.getGroupId());
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new TableDataWrapper(students)));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((e.getMessage()));
        }
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createGroup(@RequestBody IGroup iGroup) {
        try {
            Group newGroup = groupService.create(converterService.convertToGroup(iGroup));
            return ResponseEntity.status(HttpStatus.OK).body(newGroup);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addMember(String id, String groupId) {
        try {
            studentService.assignGroup(id, groupId);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("Added")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> updateGroup(@RequestBody IGroup iGroup) {
        try {
            Group updatedGroup = groupService.update(converterService.convertToGroup(iGroup));
            return ResponseEntity.status(HttpStatus.OK).body(updatedGroup);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteGroup(@PathVariable String id) {
        try {
            groupService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(new ResponseMessage("success")));
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DTOGroup>> searchGroup(@RequestParam String keyword) {
        List<Group> groups = groupService.searchByName(keyword);
        List<DTOGroup> dtoGroups = new ArrayList<>();
        groups.forEach(group -> dtoGroups.add(converterService.convertToDTOGroup(group)));
        HttpStatus httpStatus = (groups.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(dtoGroups);
    }

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteMembers(@RequestBody IInvitation iInvitation) {
        try {
            List<Request> requests = requestService.createInvitation(iInvitation.getGroupId(), iInvitation.getMembers(), iInvitation.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(requests);
        } catch (BusinessServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
